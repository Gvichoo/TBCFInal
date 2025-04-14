package com.tbacademy.nextstep.presentation.screen.main.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Reaction
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.CreateReactionUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toReactionType
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
                    val oldReaction = post.userReaction

                    // Update Local States
                    val updatedCount = when {
                        oldReaction == PostReactionType.NONE && newReaction != PostReactionType.NONE -> post.reactionCount + 1
                        oldReaction != PostReactionType.NONE && newReaction == PostReactionType.NONE -> post.reactionCount - 1
                        else -> post.reactionCount
                    }

                    post.copy(
                        userReaction = newReaction,
                        reactionCount = updatedCount,
                        wasReactionJustChanged = true
                    )
                } else post
            }
            updateState { copy(posts = updatedPosts) }

            // Debounce actual API call
            debounceReactionUpdate(postId = id, reactionType = newReaction)
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

    private fun debounceReactionUpdate(postId: String, reactionType: PostReactionType) {
        debounceJobs[postId]?.cancel()
        debounceJobs[postId] = viewModelScope.launch {
            delay(1000)
            createReaction(postId, reactionType)
            debounceJobs.remove(postId)
        }
    }

//    Api Calls

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

    private fun createReaction(postId: String, reactionType: PostReactionType) {
        viewModelScope.launch {
            createReactionUseCase(
                reaction = Reaction(
                    postId = postId,
                    type = reactionType.toReactionType()
                )
            ).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {}
                    is Resource.Error -> {
                        Log.d("PROGRESS_REACTION", "ERROR")
                        emitEffect(effect = HomeEffect.ShowError(resource.error.toMessageRes()))
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }

}