package com.tbacademy.nextstep.presentation.screen.main.home.model

import java.util.Date

data class PostPresentation(
    val id: String = "",
    val authorId: String,
    val referenceType: String, // "goal"
    val referenceId: String,
    val createdAt: Date = Date()
)