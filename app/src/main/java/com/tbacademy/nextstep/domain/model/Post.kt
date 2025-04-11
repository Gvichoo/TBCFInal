package com.tbacademy.nextstep.domain.model

import java.util.Date

data class Post(
    val id: String = "",
    val authorId: String = "",
    val referenceType: String = "",
    val referenceId: String = "",
    val createdAt: Date = Date()
)