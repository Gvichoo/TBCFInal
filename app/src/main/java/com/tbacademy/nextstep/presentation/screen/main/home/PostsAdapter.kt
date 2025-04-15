package com.tbacademy.nextstep.presentation.screen.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.ItemPostBinding
import com.tbacademy.nextstep.presentation.common.extension.animateSelected
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.screen.main.home.PostsAdapter.Companion.POPUP_VISIBILITY_CHANGED_KEY
import com.tbacademy.nextstep.presentation.screen.main.home.PostsAdapter.Companion.REACTION_CHANGED_KEY
import com.tbacademy.nextstep.presentation.screen.main.home.PostsAdapter.Companion.REACTION_COUNT_CHANGED_KEY
import com.tbacademy.nextstep.presentation.screen.main.home.extension.topReactions
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

class PostsDiffUtil : DiffUtil.ItemCallback<PostPresentation>() {
    override fun areItemsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: PostPresentation, newItem: PostPresentation): Any? {
        val diffBundle = Bundle()

        if (oldItem.userReaction != newItem.userReaction) {
            diffBundle.putBoolean(REACTION_CHANGED_KEY, true)
        }
        if (oldItem.reactionCount != newItem.reactionCount) {
            diffBundle.putBoolean(REACTION_COUNT_CHANGED_KEY, true)
        }
        if (oldItem.isReactionsPopUpVisible != newItem.isReactionsPopUpVisible) {
            diffBundle.putBoolean(POPUP_VISIBILITY_CHANGED_KEY, true)
        }

        return if (diffBundle.isEmpty) null else diffBundle
    }
}

class PostsAdapter(
    private val updateUserReaction: (postId: String, reactionType: PostReactionType?) -> Unit,
    private val reactionBtnHold: (postId: String, visible: Boolean) -> Unit
) : ListAdapter<PostPresentation, PostsAdapter.PostViewHolder>(PostsDiffUtil()) {

    companion object {
        const val REACTION_CHANGED_KEY = "reaction_changed"
        const val REACTION_COUNT_CHANGED_KEY = "reaction_count_changed"
        const val POPUP_VISIBILITY_CHANGED_KEY = "popup_visibility_changed"
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(post: PostPresentation) {
            binding.apply {
                tvAuthor.text = post.authorUsername
                tvTitle.text = post.title
                tvDescription.text = post.description
                tvDate.text = post.createdAt
                tvReactionsCount.text = post.reactionCount.toString()
                tvCommentsCount.text = post.commentCount.toString()

                // Set Active Reaction
                ivReactionIcon.animateSelected()
                setReactions(post = post)

                // Reactions Pop Up
                btnReaction.setOnLongClickListener {
                    reactionBtnHold(post.id, true)
                    true
                }
                reactionPopup.isVisible = post.isReactionsPopUpVisible

                // Handle Reaction
                btnReaction.setOnClickListener {
                    val selectedReaction = if (post.userReaction == null)
                        PostReactionType.FIRE
                    else
                        null
                    updateUserReaction(post.id, selectedReaction)
                }
            }
        }

        fun updatePartial(post: PostPresentation, payload: Bundle) {
            payload.apply {
                binding.apply {
                    if (getBoolean(REACTION_CHANGED_KEY) || getBoolean(REACTION_COUNT_CHANGED_KEY)) {

                        ivReactionIcon.animateSelected()
                        setReactions(post = post)

                        // Set Reaction and comment count
                        tvReactionsCount.text = post.reactionCount.toString()
                        tvCommentsCount.text = post.commentCount.toString()

                        // Handle Reaction
                        btnReaction.setOnClickListener {
                            val selectedReaction = if (post.userReaction == null)
                                PostReactionType.FIRE
                            else
                                null
                            updateUserReaction(post.id, selectedReaction)
                        }
                    }
                }

                if (getBoolean(POPUP_VISIBILITY_CHANGED_KEY)) {
                    binding.reactionPopup.isVisible = post.isReactionsPopUpVisible
                }
            }
        }

        private fun setReactions(post: PostPresentation) = with(binding) {
            // Active reaction
            post.userReaction?.let {
                ivReactionIcon.setImageResource(it.iconRes)
                tvReactionText.text = itemView.context.getString(it.titleRes)
            } ?: run {
                tvReactionText.text = itemView.context.getString(R.string.react)
                ivReactionIcon.setImageResource(PostReactionType.FIRE.iconRes)
            }

            post.imageUrl?.let { ivPostImage.loadImagesGlide(url = it) }

            val tint = if (post.userReaction != null)
                ContextCompat.getColor(itemView.context, R.color.md_theme_tertiary)
            else
                ContextCompat.getColor(itemView.context, R.color.md_theme_onSurfaceVariant)
            ivReactionIcon.setColorFilter(tint)

            // Post Reactions
            val topReactions = post.topReactions()

            // Top 3 Reactions
            ivReaction1.setImageResource(
                topReactions.getOrNull(0)?.first?.iconRes ?: R.drawable.ic_reaction_fire_24px
            )
            ivReaction1.setBackgroundResource(
                topReactions.getOrNull(0)?.first?.backgroundRes
                    ?: R.drawable.bg_reaction_fire
            )

            ivReaction2.isVisible = topReactions.size > 1
            ivReaction2.setImageResource(topReactions.getOrNull(1)?.first?.iconRes ?: 0)
            ivReaction2.setBackgroundResource(topReactions.getOrNull(1)?.first?.backgroundRes ?: 0)

            ivReaction3.isVisible = topReactions.size > 2
            ivReaction3.setImageResource(topReactions.getOrNull(2)?.first?.iconRes ?: 0)
            ivReaction3.setBackgroundResource(topReactions.getOrNull(2)?.first?.backgroundRes ?: 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding: ItemPostBinding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, payloads: List<Any>) {
        val post = getItem(position)
        if (payloads.isNotEmpty()) {
            val payload = payloads[0] as? Bundle ?: return
            holder.updatePartial(post = post, payload)
        } else {
            holder.onBind(post)
        }
    }
}