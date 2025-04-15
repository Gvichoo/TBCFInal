package com.tbacademy.nextstep.data.repository.reaction

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.remote.dto.ReactionDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Reaction
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReactionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ReactionRepository {
    override suspend fun createOrUpdateReaction(reaction: Reaction): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    emit(Resource.Error(ApiError.Unauthorized))
                    return@flow
                }

                val reactionsQuery = firestore.collection("reactions")
                    .whereEqualTo("postId", reaction.postId)
                    .whereEqualTo("authorId", currentUser.uid)
                    .get()
                    .await()

                val existingDoc = reactionsQuery.documents.firstOrNull()

                when {
                    // Delete
                    reaction.type == ReactionType.NONE && existingDoc != null -> {
                        existingDoc.reference.delete().await()
                    }

                    // Update
                    existingDoc != null -> {
                        existingDoc.reference.update("type", reaction.type).await()
                    }

                    // Create
                    else -> {
                        val reactionRef = firestore.collection("reactions").document()
                        val reactionDto = ReactionDto(
                            id = reactionRef.id,
                            postId = reaction.postId,
                            authorId = currentUser.uid,
                            type = reaction.type
                        )
                        reactionRef.set(reactionDto).await()
                    }
                }

                emit(Resource.Success(true))
            } catch (e: Exception) {
                emit(Resource.Error(e.toApiError()))
            } finally {
                emit(Resource.Loading(false))
            }
        }
}