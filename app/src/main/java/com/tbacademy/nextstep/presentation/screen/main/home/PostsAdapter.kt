package com.tbacademy.nextstep.presentation.screen.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemPostBinding
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

class PostsDiffUtil : DiffUtil.ItemCallback<PostPresentation>() {
    override fun areItemsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
        return oldItem == newItem
    }

}

class PostsAdapter : ListAdapter<PostPresentation, PostsAdapter.PostViewHolder>(PostsDiffUtil()) {
    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(postPresentation: PostPresentation) {
            binding.apply {
                tvAuthor.text = postPresentation.authorUsername
                tvTitle.text = postPresentation.title
                tvDescription.text = postPresentation.description
                tvDate.text = postPresentation.createdAt.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding: ItemPostBinding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBind(
            postPresentation = getItem(position)
        )
    }
}