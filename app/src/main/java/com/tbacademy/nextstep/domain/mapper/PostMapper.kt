package com.tbacademy.nextstep.domain.mapper

import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.model.Post

fun PostDto.toDomain(): Post {
    return Post(
        id = id,
        title = title,
        description = description,
        authorUsername = authorUsername,
        authorId = authorId,
        type = referenceType,
        commentCount = commentCount,
        reactionCount = reactionCount,
        imageUrl = imageUrl,
        createdAt = createdAt.toDate()
    )
}