package com.tbacademy.nextstep.presentation.extension

import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType

fun Int.adjustCount(
    old: PostReactionType?,
    new: PostReactionType?,
    target: PostReactionType
): Int {
    return when {
        old == target && new != target -> this - 1
        old != target && new == target -> this + 1
        else -> this
    }
}