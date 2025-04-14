package com.tbacademy.nextstep.presentation.common.extension

import android.widget.ImageView

fun ImageView.animateSelected(
    bounce: Float = 1.0f,
    duration: Long = 300L
) {
    this.animate()
        .scaleX(1.2f)
        .scaleY(1.2f)
        .setDuration(duration)
        .withEndAction {
            this.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(duration / 2)
                .start()
        }
        .start()
}