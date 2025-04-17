package com.tbacademy.nextstep.presentation.screen.main.home.comment.effect

sealed interface CommentsEffect {
    data object StartTyping: CommentsEffect
}