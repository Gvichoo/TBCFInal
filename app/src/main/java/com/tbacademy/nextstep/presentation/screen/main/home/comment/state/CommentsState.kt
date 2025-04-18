package com.tbacademy.nextstep.presentation.screen.main.home.comment.state

import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.presentation.screen.main.home.comment.model.CommentPresentation

data class CommentsState(
    val postId: String = "",
    val comments: List<CommentPresentation>? = null,
    val error: ApiError? = null,
    val fetchLoading: Boolean = false,
    val uploadLoading: Boolean = false
)