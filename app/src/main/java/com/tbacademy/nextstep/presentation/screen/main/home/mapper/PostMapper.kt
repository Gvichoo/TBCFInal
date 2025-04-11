package com.tbacademy.nextstep.presentation.screen.main.home.mapper

import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

fun Post.toPresentation(): PostPresentation {
    return PostPresentation(
        id, authorId, referenceType, referenceId, createdAt
    )
}