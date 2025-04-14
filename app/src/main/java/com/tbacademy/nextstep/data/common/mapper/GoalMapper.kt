package com.tbacademy.nextstep.data.common.mapper

import com.google.firebase.Timestamp
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.domain.model.Goal

fun Goal.toDto(): GoalDto {
    return GoalDto(
        id = id,
        title = title,
        description = description,
        targetDate = Timestamp(targetDate),
        isMetricBased = isMetricBased,
        metricUnit = metricUnit,
        metricTarget = metricTarget,
        createdAt = createdAt,
        imageUrl = imageUrl
    )
}