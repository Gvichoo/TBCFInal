package com.tbacademy.nextstep.data.remote.dto

import com.google.firebase.Timestamp
import java.util.Date

data class GoalDto(
    val id: String = "",
    val authorId: String = "",
    val authorUsername: String = "",
    val title: String = "",
    val description: String? = null,
    val isMetricBased: Boolean = false,
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Timestamp,
    val createdAt: Date = Date(),
    val imageUrl: String? = null
)