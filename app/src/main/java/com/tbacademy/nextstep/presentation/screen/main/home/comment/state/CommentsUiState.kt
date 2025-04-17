package com.tbacademy.nextstep.presentation.screen.main.home.comment.state

data class CommentsUiState(
    val comment: String = ""
) {
    val isSendEnabled: Boolean
        get() = comment.isNotEmpty()
}