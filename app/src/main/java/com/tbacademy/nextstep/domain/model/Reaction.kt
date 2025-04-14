package com.tbacademy.nextstep.domain.model

data class Reaction(
    val postId: String,
    val type: ReactionType,
)