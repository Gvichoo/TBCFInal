package com.tbacademy.nextstep.domain.usecase.comment

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateCommentUseCase {
    suspend operator fun invoke(postId: String, text: String): Flow<Resource<Boolean>>
}

class CreateCommentUseCaseImpl @Inject constructor(
    private val createCommentRepository: CommentRepository
) : CreateCommentUseCase {
    override suspend fun invoke(postId: String, text: String): Flow<Resource<Boolean>> {
        return createCommentRepository.createComment(postId = postId, text = text)
    }
}