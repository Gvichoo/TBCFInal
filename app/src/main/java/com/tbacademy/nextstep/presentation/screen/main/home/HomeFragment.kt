package com.tbacademy.nextstep.presentation.screen.main.home

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.databinding.FragmentHomeBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.home.adapter.PostsAdapter
import com.tbacademy.nextstep.presentation.screen.main.home.comment.CommentsSheetFragment
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val postsAdapter: PostsAdapter by lazy {
        PostsAdapter(
            updateUserReaction = { postId, reactionType ->
                homeViewModel.onEvent(
                    event = HomeEvent.HandleReactToPost(
                        postId = postId,
                        reactionType = reactionType
                    )
                )
            },
            reactionBtnHold = { postId, visible ->
                homeViewModel.onEvent(
                    event = HomeEvent.ToggleReactionsSelector(
                        postId = postId,
                        visible = visible
                    )
                )
            },
            commentsClicked = { postId ->
                homeViewModel.onEvent(event = HomeEvent.OpenPostComments(postId = postId))
            },
            commentsIconClicked = { postId ->
                homeViewModel.onEvent(
                    event = HomeEvent.OpenPostComments(
                        postId = postId,
                        typeActive = true
                    )
                )
            }
        )
    }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun start() {
        homeViewModel.onEvent(HomeEvent.FetchGlobalPosts)

        setPostsAdapter()
    }

    override fun listeners() {
    }

    override fun observers() {
        observeState()
        observeEffects()
    }

    private fun observeState() {
        collectLatest(flow = homeViewModel.state) { state ->
            if (state.posts != null) {
                postsAdapter.submitList(state.posts)
            }
            binding.pbPosts.isVisible = state.isLoading

            Log.d("HOME_STATE", "$state")
        }
    }

    private fun observeEffects() {
        collect(flow = homeViewModel.effects) { effect ->
            when (effect) {
                is HomeEffect.ShowError -> effect.errorRes?.let { showMessage(message = it) }
                is HomeEffect.OpenComments -> openCommentsBottomSheet(
                    postId = effect.postId,
                    typeActive = effect.typeActive
                )
            }
        }
    }

    private fun openCommentsBottomSheet(postId: String, typeActive: Boolean) {
        val commentsSheet = CommentsSheetFragment()
        commentsSheet.arguments = Bundle().apply {
            putString("postId", postId)
            putBoolean("typeActive", typeActive)
        }
        commentsSheet.show(childFragmentManager, "CommentsSheet")
    }

    private fun setPostsAdapter() {
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = postsAdapter
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}