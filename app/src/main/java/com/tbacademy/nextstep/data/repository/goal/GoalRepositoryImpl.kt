package com.tbacademy.nextstep.data.repository.goal

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDto
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : GoalRepository {
    override suspend fun createGoal(goal: Goal): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(loading = true))
        try {
            val currentUser = firebaseAuth.currentUser
            val userSnapshot: DocumentSnapshot? =
                currentUser?.let { firestore.collection("users").document(it.uid).get().await() }

            if (userSnapshot == null) {
                emit(Resource.Error(ApiError.Unauthorized))
                emit(Resource.Loading(false))
                return@flow
            }

            val goalRef = firestore.collection("goals").document()
            val goalId = goalRef.id
            val username: String? = userSnapshot.getString("username")

            if (username != null) {
                val goalDto: GoalDto = goal.toDto().copy(
                    authorId = currentUser.uid,
                    authorUsername = username,
                    id = goalId
                )

                // Upload goal to Firestore
                goalRef.set(goalDto).await()
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(ApiError.Unauthorized))
                return@flow
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
            Log.d("CREATE_GOAL", "GoalId: $e")
        } finally {
            emit(Resource.Loading(false))
        }
    }
}