package com.tbacademy.nextstep.domain.repository.post

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(): Flow<Resource<List<Post>>>
}