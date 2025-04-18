package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

fun PostReactionType.toDomain(): ReactionType = when (this) {
    PostReactionType.FIRE -> ReactionType.FIRE
    PostReactionType.HEART -> ReactionType.HEART
    PostReactionType.COOKIE -> ReactionType.COOKIE
    PostReactionType.CHEER -> ReactionType.CHEER
    PostReactionType.DISAPPOINTMENT -> ReactionType.DISAPPOINTMENT
}

fun ReactionType.toPresentation(): PostReactionType = when (this) {
    ReactionType.FIRE -> PostReactionType.FIRE
    ReactionType.HEART -> PostReactionType.HEART
    ReactionType.COOKIE -> PostReactionType.COOKIE
    ReactionType.CHEER -> PostReactionType.CHEER
    ReactionType.DISAPPOINTMENT -> PostReactionType.DISAPPOINTMENT
}