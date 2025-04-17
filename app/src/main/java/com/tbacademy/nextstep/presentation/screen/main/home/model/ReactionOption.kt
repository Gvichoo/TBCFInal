package com.tbacademy.nextstep.presentation.screen.main.home.model

data class ReactionOption(
    val type: PostReactionType,
    val isSelected: Boolean = false,
    val isHovered: Boolean = false
)