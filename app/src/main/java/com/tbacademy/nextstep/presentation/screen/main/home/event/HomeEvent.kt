package com.tbacademy.nextstep.presentation.screen.main.home.event

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

sealed interface HomeEvent {
    data object FetchGlobalPosts : HomeEvent
    data class ReactToPost(val postId: String, val reactionType: PostReactionType) : HomeEvent
    data class ToggleReactionsSelector(val postId: String, val visible: Boolean) : HomeEvent
}