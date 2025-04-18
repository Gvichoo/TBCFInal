package com.tbacademy.nextstep

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.google.firebase.FirebaseApp
import com.tbacademy.nextstep.data.worker.UploadGoalDataWorker
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class App: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: MyWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        createChannels()
    }


    @Singleton
    class MyWorkerFactory @Inject constructor(
        private val createGoalUseCase: CreateGoalUseCase
    ) : WorkerFactory(){
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return when (workerClassName){
                UploadGoalDataWorker::class.java.name -> UploadGoalDataWorker(
                    appContext,
                    workerParameters,
                    createGoalUseCase
                )


                else -> {throw IllegalArgumentException("Unknown worker class name: $workerClassName")}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel = NotificationChannel(
            "channel_analytics",
            "analytics",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


}