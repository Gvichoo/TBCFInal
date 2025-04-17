package com.tbacademy.nextstep.domain.repository.comment

import com.tbacademy.nextstep.domain.core.Resource
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun createComment(postId: String, text: String): Flow<Resource<Boolean>>
}