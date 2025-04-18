package com.tbacademy.nextstep.presentation.screen.main.home.comment.model

import java.util.Date

data class CommentPresentation(
    val id: String,
    val postId: String,
    val authorId: String,
    val authorUsername: String,
    val authorProfilePictureUrl: String? = null,
    val text: String,
    val createdAt: String
)