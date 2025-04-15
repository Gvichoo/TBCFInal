package com.tbacademy.nextstep.domain.repository.reaction

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Reaction
import com.tbacademy.nextstep.domain.model.ReactionType
import kotlinx.coroutines.flow.Flow

interface ReactionRepository {
    suspend fun createReaction(postId: String, type: ReactionType): Flow<Resource<Boolean>>
//    suspend fun updateReaction(reactionId: String, newType: ReactionType): Resource<Boolean>
//    suspend fun deleteReaction(reactionId: String): Resource<Boolean>
}