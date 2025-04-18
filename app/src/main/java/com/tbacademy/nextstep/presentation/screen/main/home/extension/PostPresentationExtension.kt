package com.tbacademy.nextstep.presentation.screen.main.home.extension

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

fun PostPresentation.topReactions(): List<Pair<PostReactionType, Int>> {
    return listOf(
        PostReactionType.FIRE to reactionFireCount,
        PostReactionType.HEART to reactionHeartCount,
        PostReactionType.COOKIE to reactionCookieCount,
        PostReactionType.CHEER to reactionCheerCount,
        PostReactionType.DISAPPOINTMENT to reactionDisappointmentCount
    ).filter { it.second > 0 }
        .sortedByDescending { it.second }
        .take(3)
}