package com.tbacademy.nextstep.presentation.screen.main.home.comment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentCommentsSheetBinding
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.home.comment.effect.CommentsEffect
import com.tbacademy.nextstep.presentation.screen.main.home.comment.event.CommentsEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommentsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentsSheetBinding? = null
    private val binding: FragmentCommentsSheetBinding get() = _binding!!

    private val commentsViewModel: CommentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeners()
        observers()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val postId = requireArguments().getString(ARG_POST_ID) ?: return@repeatOnLifecycle
                val shouldTypingStart = requireArguments().getBoolean(ARG_TYPE_ACTIVE)
                commentsViewModel.onEvent(CommentsEvent.UpdatePostId(postId))
                if (shouldTypingStart) {
                    commentsViewModel.onEvent(CommentsEvent.StartTyping)
                }
            }
        }
    }

    private fun listeners() {
        setCommentInputListener()
        setSendBtnListener()
    }

    private fun observers() {
        observeUiState()
        observeEffects()
    }

    private fun observeUiState() {
        collect(flow = commentsViewModel.uiState) { state ->
            binding.btnSend.isVisible = state.isSendEnabled
        }
    }

    private fun observeEffects() {
        collect(flow = commentsViewModel.effects) { effect ->
            Log.d("EFFECT_RECEIVED", "FINALLY")
            when (effect) {
                is CommentsEffect.StartTyping -> {
                    binding.etComment.requestFocus()
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.etComment, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
    }

    private fun setSendBtnListener() {
        binding.btnSend.setOnClickListener {
            commentsViewModel.onEvent(CommentsEvent.CreateComment)
        }
    }

    private fun setCommentInputListener() {
        binding.etComment.onTextChanged { comment ->
            commentsViewModel.onEvent(CommentsEvent.CommentChanged(comment = comment))
        }
    }

    companion object {
        const val ARG_POST_ID = "postId"
        const val ARG_TYPE_ACTIVE = "typeActive"
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                sheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                BottomSheetBehavior.from(sheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}