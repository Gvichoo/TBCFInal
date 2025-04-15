package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

fun PostReactionType.toDomain(): ReactionType = when (this) {
    PostReactionType.NONE -> ReactionType.NONE
    PostReactionType.FIRE -> ReactionType.FIRE
    PostReactionType.HEART -> ReactionType.HEART
    PostReactionType.COOKIE -> ReactionType.COOKIE
    PostReactionType.CHEER -> ReactionType.CHEER
    PostReactionType.TASK -> ReactionType.TASK
}

fun ReactionType.toPresentation(): PostReactionType = when (this) {
    ReactionType.NONE -> PostReactionType.NONE
    ReactionType.FIRE -> PostReactionType.FIRE
    ReactionType.HEART -> PostReactionType.HEART
    ReactionType.COOKIE -> PostReactionType.COOKIE
    ReactionType.CHEER -> PostReactionType.CHEER
    ReactionType.TASK -> PostReactionType.TASK
}