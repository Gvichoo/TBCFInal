package com.tbacademy.nextstep.presentation.screen.main.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.CreateReactionUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.adjustCount
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toDomain
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType
import com.tbacademy.nextstep.presentation.screen.main.home.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val createReactionUseCase: CreateReactionUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect, Unit>(
    initialState = HomeState(),
    initialUiState = Unit
) {
    private val debounceJobs = mutableMapOf<String, Job>()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchGlobalPosts -> getGlobalPosts()
            is HomeEvent.HandleReactToPost -> reactToPost(
                id = event.postId,
                newReaction = event.reactionType
            )

            is HomeEvent.ToggleReactionsSelector -> toggleReactionSelector(
                id = event.postId,
                visible = event.visible
            )
        }
    }

    private fun reactToPost(id: String, newReaction: PostReactionType?) {
        Log.d("REACT_TO_POST_VIEWMODEL", "$newReaction")
        val updatedPosts = state.value.posts?.map { post ->
            if (post.id != id) return@map post

            val oldReaction = post.userReaction

            val isAdding = oldReaction == null && newReaction != null
            val isRemoving = oldReaction != null && newReaction == null

            val newTotalCount = when {
                isAdding -> post.reactionCount + 1
                isRemoving -> post.reactionCount - 1
                else -> post.reactionCount
            }

            handleReactionDebounce(old = oldReaction, new = newReaction, postId = post.id)

            post.copy(
                userReaction = newReaction,
                reactionCount = newTotalCount,
                reactionFireCount = post.reactionFireCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.FIRE
                ),
                reactionHeartCount = post.reactionHeartCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.HEART
                ),
                reactionCookieCount = post.reactionCookieCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.COOKIE
                ),
                reactionCheerCount = post.reactionCheerCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.CHEER
                ),
                reactionTaskCount = post.reactionTaskCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.TASK
                ),
                wasReactionJustChanged = true
            )
        } ?: return

        updateState { copy(posts = updatedPosts) }
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

    private fun handleReactionDebounce(old: PostReactionType?, new: PostReactionType?, postId: String) {
        when {
            // Create
            old == null && new != null -> createReactionDebounce(postId, new)
            //old != null && new == null -> deleteReactionDebounce(postId)
            //old != null && new != null && old != new -> updateReactionDebounce(postId, new)
        }
    }

    private fun createReactionDebounce(postId: String, reactionType: PostReactionType) {
        debounceJobs[postId]?.cancel()
        debounceJobs[postId] = viewModelScope.launch {
            delay(150)
            createReaction(postId, reactionType)
            debounceJobs.remove(postId)
        }
    }

//    Api Calls

    private fun getGlobalPosts() {
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

    private fun createReaction(postId: String, reactionType: PostReactionType) {
        viewModelScope.launch {
            createReactionUseCase(
                postId = postId,
                reactionType = reactionType.toDomain()
            ).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {}
                    is Resource.Error -> {
                        emitEffect(effect = HomeEffect.ShowError(resource.error.toMessageRes()))
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

}