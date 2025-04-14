package com.tbacademy.nextstep.presentation.screen.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
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
            diffBundle.putBoolean("reaction_changed", true)
        }
        if (oldItem.reactionCount != newItem.reactionCount) {
            diffBundle.putBoolean("reaction_count_changed", true)
        }
        if (oldItem.isReactionsPopUpVisible != newItem.isReactionsPopUpVisible) {
            diffBundle.putBoolean("popup_visibility_changed", true)
        }

        return if (diffBundle.isEmpty) null else diffBundle
    }
}

class PostsAdapter(
    private val updateUserReaction: (postId: String, reactionType: PostReactionType) -> Unit,
    private val reactionBtnHold: (postId: String, visible: Boolean) -> Unit
) : ListAdapter<PostPresentation, PostsAdapter.PostViewHolder>(PostsDiffUtil()) {
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
                ivReactionIcon.setImageResource(post.userReaction.iconRes)
                reactionPopup.isVisible = post.isReactionsPopUpVisible
                
                post.imageUrl?.let { ivPostImage.loadImagesGlide(url = it) }


                val tint = if (post.userReaction != PostReactionType.NONE)
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.md_theme_tertiary
                    ) else ContextCompat.getColor(
                    itemView.context,
                    R.color.md_theme_onSurfaceVariant
                )
                ivReactionIcon.setColorFilter(tint)

                btnReaction.setOnLongClickListener {
                    reactionBtnHold(post.id, true)
                    true
                }

                // Handle Reaction
                btnReaction.setOnClickListener {
                    val selectedReaction = if (post.userReaction == PostReactionType.NONE)
                        PostReactionType.FIRE
                    else
                        PostReactionType.NONE
                    updateUserReaction(post.id, selectedReaction)
                }

                setUpReactionPopupListeners(binding = binding, post = post)
            }
        }

        fun updatePartial(post: PostPresentation, payload: Bundle) {
            payload.apply {
                binding.apply {
                    if (getBoolean("reaction_changed") || getBoolean("reaction_count_changed")) {

                        ivReactionIcon.animateSelected()
                        ivReactionIcon.setImageResource(post.userReaction.iconRes)
                        tvReactionsCount.text = post.reactionCount.toString()
                        tvCommentsCount.text = post.commentCount.toString()

                        val tint = if (post.userReaction != PostReactionType.NONE)
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.md_theme_tertiary
                            ) else ContextCompat.getColor(
                            itemView.context,
                            R.color.md_theme_onSurfaceVariant
                        )
                        ivReactionIcon.setColorFilter(tint)

                        tvReactionsCount.text = post.reactionCount.toString()
                        ivReactionIcon.animateSelected()

                        // Handle Reaction
                        btnReaction.setOnClickListener {
                            val selectedReaction = if (post.userReaction == PostReactionType.NONE)
                                PostReactionType.FIRE
                            else
                                PostReactionType.NONE
                            updateUserReaction(post.id, selectedReaction)
                        }
                    }
                }

                if (getBoolean("popup_visibility_changed")) {
                    binding.reactionPopup.isVisible = post.isReactionsPopUpVisible
                }
            }
        }
    }

    private fun setUpReactionPopupListeners(
        post: PostPresentation,
        binding: ItemPostBinding
    ) {
        with(binding) {

            val reactionViews: Map<PostReactionType, View> = mapOf(
                PostReactionType.FIRE to binding.reactionFire,
                PostReactionType.HEART to binding.reactionHeart,
                PostReactionType.COOKIE to binding.reactionCookie,
                PostReactionType.CHEER to binding.reactionCheer,
                PostReactionType.TASK to binding.reactionTask
            )

            var hoveredReaction: PostReactionType = PostReactionType.NONE

            val onTouchListener = View.OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        hoveredReaction = reactionViews.entries.firstOrNull { (_, view) ->
                            isTouchNear(view, event)
                        }?.key ?: PostReactionType.NONE

                        // Animate hover
                        reactionViews.forEach { (type, view) ->
                            val isHovered = type == hoveredReaction
                            view.animate()
                                .scaleX(if (isHovered) 1.5f else 1f)
                                .scaleY(if (isHovered) 1.5f else 1f)
                                .setDuration(100)
                                .start()
                        }

                        Log.d("HOVERED_REACTION", hoveredReaction.name)

                    }

                    MotionEvent.ACTION_UP -> {
                        v.parent.requestDisallowInterceptTouchEvent(false) // allow scroll again
                        v.performClick()
                        // User lifted finger → confirm selection
                        if (hoveredReaction != PostReactionType.NONE) {
                            updateUserReaction(post.id, hoveredReaction)
                        }

                        // Hide popup
                        reactionBtnHold(post.id, false)
                    }
                }
                true
            }
            reactionPopup.setOnTouchListener(onTouchListener)
        }
    }

    private fun isTouchNear(
        view: View,
        event: MotionEvent,
        xRadius: Int = 80,
        ySlack: Int = 500
    ): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewCenterX = location[0] + view.width / 2
        val viewCenterY = location[1] + view.height / 2

        val dx = event.rawX - viewCenterX
        val dy = event.rawY - viewCenterY

        return kotlin.math.abs(dx) <= xRadius && kotlin.math.abs(dy) <= ySlack
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