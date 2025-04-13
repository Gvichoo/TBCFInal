package com.tbacademy.nextstep.presentation.screen.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.R
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

class PostsAdapter(
    private val reactionBtnClick: (String) -> Unit
) : ListAdapter<PostPresentation, PostsAdapter.PostViewHolder>(PostsDiffUtil()) {
    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(postPresentation: PostPresentation) {
            var postLiked: Boolean = false
            binding.apply {
                tvAuthor.text = postPresentation.authorUsername
                tvTitle.text = postPresentation.title
                tvDescription.text = postPresentation.description
                tvDate.text = postPresentation.createdAt
                tvReactionsCount.text = postPresentation.reactionCount.toString()
                tvCommentsCount.text = postPresentation.commentCount.toString()

                btnReaction.setOnClickListener {
                    val reactionCount = if (!postLiked) postPresentation.reactionCount + 1 else postPresentation.reactionCount
                    postLiked = !postLiked
                    if (postLiked) {
                        ivReactionIcon.setImageResource(R.drawable.ic_flame_active)
                    } else {
                        ivReactionIcon.setImageResource(R.drawable.ic_flame)
                    }
                    reactionBtnClick(postPresentation.id)
                    tvReactionsCount.text = reactionCount.toString()
                }
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