package com.tbacademy.nextstep.presentation.screen.main.home.event

sealed interface HomeEvent {
    data object FetchGlobalPosts: HomeEvent
    data class ReactToPost(val postId: String): HomeEvent
}