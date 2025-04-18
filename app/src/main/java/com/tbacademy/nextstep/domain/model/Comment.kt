package com.tbacademy.nextstep.domain.model

import java.util.Date

data class Comment(
    val id: String,
    val postId: String,
    val authorId: String,
    val authorUsername: String,
    val authorProfilePictureUrl: String? = null,
    val text: String,
    val createdAt: Date
)