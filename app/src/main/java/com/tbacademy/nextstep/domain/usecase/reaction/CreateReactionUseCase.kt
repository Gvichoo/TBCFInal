package com.tbacademy.nextstep.domain.usecase.reaction

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Reaction
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface CreateReactionUseCase {
    suspend operator fun invoke(reaction: Reaction): Flow<Resource<Boolean>>
}

class CreateReactionUseCaseImpl @Inject constructor(
    private val reactionRepository: ReactionRepository
): CreateReactionUseCase {
    override suspend fun invoke(reaction: Reaction): Flow<Resource<Boolean>> {
        return reactionRepository.createOrUpdateReaction(reaction = reaction)
    }
}

