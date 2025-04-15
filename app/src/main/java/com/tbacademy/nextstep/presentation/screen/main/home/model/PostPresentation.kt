package com.tbacademy.nextstep.presentation.screen.main.home.model

data class PostPresentation(
    val id: String,
    val authorId: String,
    val authorUsername: String,
    val title: String,
    val description: String,
    val reactionCount: Int,
    val reactionFireCount: Int = 0,
    val reactionHeartCount: Int = 0,
    val reactionCookieCount: Int = 0,
    val reactionCheerCount: Int = 0,
    val reactionTaskCount: Int = 0,
    val commentCount: Int,
    val type: String,
    val imageUrl: String? = null,
    val createdAt: String,
    val userReaction: PostReactionType = PostReactionType.NONE,
    val isReactionsPopUpVisible: Boolean = false,
    val wasReactionJustChanged: Boolean = false
)