package com.tbacademy.nextstep.domain.model

import android.net.Uri
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import java.util.Date

data class Goal(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val isMetricBased: Boolean = false,
    val metricTarget: String? = null,
    val metricUnit: String? = null,
    val targetDate: Date,
    val createdAt: Date = Date(),
    val imageUri: Uri? = null,
    val milestone : List<MilestoneItem>? = null
){
    val imageUrl: String? = null
}