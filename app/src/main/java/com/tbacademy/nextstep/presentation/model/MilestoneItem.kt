package com.tbacademy.nextstep.presentation.model

data class MilestoneItem(
    val id: Int,
    var text: String,
    val errorMessage: Int? = null
)