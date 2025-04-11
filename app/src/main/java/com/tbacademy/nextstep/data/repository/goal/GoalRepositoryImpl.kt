package com.tbacademy.nextstep.data.repository.goal

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
): GoalRepository {
    override suspend fun createGoal(goal: Goal): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(loading = true))
        Log.d("REPOsss", "Started creating goal")
        try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                emit(Resource.Error(ApiError.Unauthorized))
                emit(Resource.Loading(false))
                return@flow
            }

            val goalRef = firestore.collection("goals").document()
            val goalId = goalRef.id

            Log.d("CREATE_GOAL", "GoalId: $goalId")

            val goalWithId = goal.copy(
                id = goalId,
                authorId = currentUser.uid,
                createdAt = Date()
            )

            // Upload goal to Firestore
            goalRef.set(goalWithId).await()

            // Create linked post
            val postRef = firestore.collection("posts").document()
            val post = Post(
                id = postRef.id,
                authorId = currentUser.uid,
                referenceType = "goal",
                referenceId = goalId,
                createdAt = Date()
            )

            postRef.set(post).await()

            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
            Log.d("CREATE_GOAL", "GoalId: $e")
        } finally {
            emit(Resource.Loading(false))
        }
    }
}