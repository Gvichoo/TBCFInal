package com.tbacademy.nextstep.domain.model

import java.util.Date

data class Goal(
    val id: String = "",
    val authorId: String = "",
    val title: String = "",
    val description: String? = null,
    val isMetricBased: Boolean = false,
    val metricTarget: Float? = null,
    val metricUnit: String? = null,
    val targetDate: Date,
    val createdAt: Date = Date()
)