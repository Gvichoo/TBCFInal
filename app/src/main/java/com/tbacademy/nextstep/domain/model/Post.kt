package com.tbacademy.nextstep.domain.model

import java.util.Date

data class Post(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val title: String,
    val description: String,
    val reactionCount: Int,
    val commentCount: Int,
    val type: String,
    val createdAt: Date
)