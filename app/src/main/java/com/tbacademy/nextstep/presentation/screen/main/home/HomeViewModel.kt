package com.tbacademy.nextstep.presentation.screen.main.home

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
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
            is HomeEvent.ReactToPost -> {}
        }
    }

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

}