package com.tbacademy.nextstep.presentation.screen.main.home.comment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.usecase.comment.CreateCommentUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.home.comment.effect.CommentsEffect
import com.tbacademy.nextstep.presentation.screen.main.home.comment.event.CommentsEvent
import com.tbacademy.nextstep.presentation.screen.main.home.comment.state.CommentsState
import com.tbacademy.nextstep.presentation.screen.main.home.comment.state.CommentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val createCommentUseCase: CreateCommentUseCase
) :
    BaseViewModel<CommentsState, CommentsEvent, CommentsEffect, CommentsUiState>(
        initialState = CommentsState(),
        initialUiState = CommentsUiState()
    ) {
    override fun onEvent(event: CommentsEvent) {
        when (event) {
            is CommentsEvent.CommentChanged -> onCommentChanged(comment = event.comment)
            is CommentsEvent.UpdatePostId -> setPostId(postId = event.postId)
            is CommentsEvent.StartTyping -> sendStartTypingEffect()
            is CommentsEvent.CreateComment -> createComment()
        }
    }

    private fun createComment() {
        viewModelScope.launch {
            createCommentUseCase(
                postId = state.value.postId,
                text = uiState.value.comment
            ).collectLatest { resource ->
                Log.d("RESOURCE_CREATE", "$resource")

            }
        }
    }

    private fun setPostId(postId: String) {
        updateState { this.copy(postId = postId) }
    }

    private fun sendStartTypingEffect() {
        viewModelScope.launch {
            Log.d("SHOULD_START_TYPING", "TRUE_VIEW")
            emitEffect(effect = CommentsEffect.StartTyping)
        }
    }

    private fun onCommentChanged(comment: String) {
        updateUiState { this.copy(comment = comment) }
    }
}