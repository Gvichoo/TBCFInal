package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.CommentDto
import com.tbacademy.nextstep.domain.model.Comment

fun CommentDto.toDomain(): Comment {
    return Comment(
        id,
        postId,
        authorId,
        authorUsername,
        authorProfilePictureUrl,
        text,
        createdAt = createdAt.toDate()
    )
}