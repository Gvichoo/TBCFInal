package com.tbacademy.nextstep.presentation.common.extension

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible

fun View.animatePopupIn() {
    alpha = 0f
    scaleX = 0.8f
    scaleY = 0.8f
    visibility = View.VISIBLE

    animate()
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(150)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}

fun View.animatePopIn(delay: Long = 0L) {
    scaleX = 0f
    scaleY = 0f
    alpha = 0f

    animate()
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
        .setStartDelay(delay)
        .setDuration(250)
        .setInterpolator(OvershootInterpolator())
        .start()
}

fun View.animateFadeOut(onEnd: () -> Unit) {
    this.animate()
        .alpha(0f)
        .scaleX(0.7f)
        .scaleY(0.7f)
        .setDuration(150)
        .withEndAction {
            this.isVisible = false
            this.alpha = 1f
            this.scaleX = 1f
            this.scaleY = 1f
            onEnd()
        }
}
