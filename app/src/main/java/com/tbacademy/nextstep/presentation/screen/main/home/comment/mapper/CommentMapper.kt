package com.tbacademy.nextstep.presentation.screen.main.home.comment.mapper

import com.tbacademy.nextstep.domain.model.Comment
import com.tbacademy.nextstep.presentation.common.extension.toTimeAgo
import com.tbacademy.nextstep.presentation.screen.main.home.comment.model.CommentPresentation

fun Comment.toPresentation(): CommentPresentation {
    return CommentPresentation(
        id,
        postId,
        authorId,
        authorUsername,
        authorProfilePictureUrl,
        text,
        createdAt.toTimeAgo()
    )
}