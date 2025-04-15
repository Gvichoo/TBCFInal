package com.tbacademy.nextstep.presentation.extension

fun Int.incrementIf(oldIsType: Boolean, newIsType: Boolean): Int {
    return when {
        oldIsType && !newIsType -> this - 1
        !oldIsType && newIsType -> this + 1
        else -> this
    }
}