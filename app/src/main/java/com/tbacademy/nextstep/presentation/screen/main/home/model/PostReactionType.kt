package com.tbacademy.nextstep.presentation.screen.main.home.model

import com.tbacademy.nextstep.R

enum class PostReactionType(val iconRes: Int) {
    NONE(iconRes = R.drawable.ic_reaction_fire_24px),
    FIRE(iconRes = R.drawable.ic_reaction_fire_24px),
    HEART(iconRes = R.drawable.ic_reaction_heart_24px),
    COOKIE(iconRes = R.drawable.ic_reaction_cookie_24px),
    CHEER(iconRes = R.drawable.ic_reaction_cheer_24px),
    TASK(iconRes = R.drawable.ic_reaction_task_24px)
}