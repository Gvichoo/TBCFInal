package com.tbacademy.nextstep.presentation.screen.main.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType
import com.tbacademy.nextstep.presentation.screen.main.home.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect, Unit>(
    initialState = HomeState(),
    initialUiState = Unit
) {
    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchGlobalPosts -> getGlobalPosts()
            is HomeEvent.ReactToPost -> reactToPost(
                id = event.postId,
                newReaction = event.reactionType
            )

            is HomeEvent.ToggleReactionsSelector -> toggleReactionSelector(
                id = event.postId,
                visible = event.visible
            )
        }
    }

    private fun reactToPost(id: String, newReaction: PostReactionType) {
        state.value.posts?.let { posts ->
            val updatedPosts = posts.map { post ->
                if (post.id == id) {
                    val currentReaction = post.userReaction

                    val updatedReaction = if (currentReaction == newReaction) {
                        PostReactionType.NONE
                    } else {
                        newReaction
                    }

                    val updatedCount = when {
                        currentReaction == PostReactionType.NONE && updatedReaction != PostReactionType.NONE -> post.reactionCount + 1
                        currentReaction != PostReactionType.NONE && updatedReaction == PostReactionType.NONE -> post.reactionCount - 1
                        else -> post.reactionCount
                    }

                    post.copy(
                        userReaction = updatedReaction,
                        reactionCount = updatedCount,
                        wasReactionJustChanged = true
                    )
                } else post
            }
            updateState { copy(posts = updatedPosts) }
        }
    }

    private fun toggleReactionSelector(id: String, visible: Boolean) {
        state.value.posts?.let { posts ->
            val updatedPosts = posts.map { post ->
                if (post.id == id) {
                    post.copy(isReactionsPopUpVisible = visible)
                } else post
            }

            updateState { copy(posts = updatedPosts) }
        }
    }

    private fun getGlobalPosts() {
        if (state.value.posts == null) {
            viewModelScope.launch {
                getPostsUseCase().collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }
                        is Resource.Success -> updateState { this.copy(posts = resource.data.map { it.toPresentation() }) }
                        is Resource.Error -> updateState { this.copy(error = resource.error) }
                    }
                }
            }
        }
    }

}