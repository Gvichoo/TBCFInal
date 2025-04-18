package com.tbacademy.nextstep.presentation.screen.main.home.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemCommentBinding
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.screen.main.home.comment.model.CommentPresentation

class CommentsDiffUtil : DiffUtil.ItemCallback<CommentPresentation>() {
    override fun areItemsTheSame(
        oldItem: CommentPresentation,
        newItem: CommentPresentation
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CommentPresentation,
        newItem: CommentPresentation
    ): Boolean {
        return oldItem == newItem
    }
}

class CommentsAdapter() :
    ListAdapter<CommentPresentation, CommentsAdapter.CommentViewHolder>(CommentsDiffUtil()) {
    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(comment: CommentPresentation) {
            binding.apply {
                tvAuthor.text = comment.authorUsername
                tvCommentBody.text = comment.text
                tvCreatedAt.text = comment.createdAt
                comment.authorProfilePictureUrl?.let { ivProfile.loadImagesGlide(url = it) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding: ItemCommentBinding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(
            comment = getItem(position)
        )
    }
}