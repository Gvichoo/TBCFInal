package com.tbacademy.nextstep.domain.repository.reaction

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Reaction
import kotlinx.coroutines.flow.Flow

interface ReactionRepository {
    suspend fun createOrUpdateReaction(reaction: Reaction): Flow<Resource<Boolean>>
}