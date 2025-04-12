package com.tbacademy.nextstep.presentation.screen.main.home.model

import java.util.Date

data class PostPresentation(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val title: String,
    val description: String,
    val reactionCount: Int,
    val commentCount: Int,
    val type: String,
    val createdAt: String
)