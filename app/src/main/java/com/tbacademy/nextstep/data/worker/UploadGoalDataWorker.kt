package com.tbacademy.nextstep.data.worker

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Date

@HiltWorker
class UploadGoalDataWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted workerParams: WorkerParameters,
    private val createGoalUseCase: CreateGoalUseCase
):CoroutineWorker(context,workerParams){
    override suspend fun doWork(): Result {
        // Extract input data
        val title = inputData.getString("title") ?: return Result.failure()
        val description = inputData.getString("description")
        val isMetricBased = inputData.getBoolean("isMetricBased", false)
        val metricTarget = inputData.getString("metricTarget")
        val metricUnit = inputData.getString("metricUnit")
        val targetDateMillis = inputData.getLong("targetDate", 0L)
        val imageUriStr = inputData.getString("imageUri")

        // Check targetDate
        val targetDate = if (targetDateMillis == 0L) {
            Date() // default fallback
        } else {
            Date(targetDateMillis)
        }

        // Build the Goal object
        val goal = Goal(
            title = title,
            description = description,
            isMetricBased = isMetricBased,
            metricTarget = metricTarget,
            metricUnit = metricUnit,
            targetDate = targetDate,
            imageUri = imageUriStr?.let { Uri.parse(it) }
        )

        return writeGoalData(goal)
    }

    private suspend fun writeGoalData(goal: Goal): Result {
        Log.d("UploadGoalWorkers", "Starting upload for goal: ${goal.title}")

        var finalResult: Result = Result.retry()

        try {
            createGoalUseCase(goal).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("UploadGoalWorkers", "Uploading goal...")
                        // Do nothing, wait for next emit
                    }
                    is Resource.Error -> {
                        Log.e("UploadGoalWorkers", "Upload failed: ${result.error}")
                        val outputData = workDataOf("error_message" to result.error)
                        finalResult = Result.failure(outputData)
                    }
                    is Resource.Success -> {
                        val notificationManager =
                            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        val notification = NotificationCompat.Builder(applicationContext, "channel_analytics")
                            .setSmallIcon(R.drawable.baseline_add_24)
                            .setContentTitle("Goal Uploaded")
                            .setContentText("Your goal \"${goal.title}\" was uploaded.")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .build()

                        notificationManager.notify(1, notification)

                        Log.d("UploadGoalWorkers", "Upload success!")
                        finalResult = Result.success()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("UploadGoalWorkers", "Exception occurred: ${e.localizedMessage}")
            finalResult = Result.retry()
        }

        return finalResult
    }




}