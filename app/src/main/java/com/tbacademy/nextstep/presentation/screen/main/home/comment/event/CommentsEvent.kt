package com.tbacademy.nextstep.presentation.screen.main.home.comment.event

sealed interface CommentsEvent {
    data class CommentChanged(val comment: String): CommentsEvent
    data class UpdatePostId(val postId: String): CommentsEvent
    data object CreateComment: CommentsEvent
    data object StartTyping: CommentsEvent
}