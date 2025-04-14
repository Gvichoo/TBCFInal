package com.tbacademy.nextstep.presentation.screen.main.home.model

data class PostPresentation(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val title: String,
    val description: String,
    val reactionCount: Int,
    val commentCount: Int,
    val type: String,
    val createdAt: String,
    val userReaction: PostReactionType = PostReactionType.NONE,
    val isReactionsPopUpVisible: Boolean = false,
    val wasReactionJustChanged: Boolean = false
)