package com.tbacademy.nextstep.data.repository.reaction

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
import com.tbacademy.nextstep.data.remote.dto.ReactionDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReactionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: FirebaseHelper
) : ReactionRepository {
    override suspend fun createReaction(
        postId: String,
        type: ReactionType
    ): Flow<Resource<Boolean>> = flow {
        firebaseHelper.withUserFlow { userId ->
            val reactionRef = firestore.collection(REACTION_COLLECTION_KEY).document()

            val reactionDto = ReactionDto(
                id = reactionRef.id,
                postId = postId,
                authorId = userId,
                type = type
            )
            reactionRef.set(reactionDto).await()
            true
        }.collectLatest {}
    }
//
//    override suspend fun updateReaction(
//        reactionId: String,
//        newType: ReactionType
//    ): Resource<Boolean> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteReaction(reactionId: String): Resource<Boolean> {
//        TODO("Not yet implemented")
//    }

    companion object {
        const val REACTION_COLLECTION_KEY = "reactions"
    }
}