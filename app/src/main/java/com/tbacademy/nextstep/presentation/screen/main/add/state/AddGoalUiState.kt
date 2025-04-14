package com.tbacademy.nextstep.presentation.screen.main.add.state

import android.net.Uri
import java.util.Date

data class AddGoalUiState (
    val title : String = "",
    val description: String = "",
    val goalDate : Date? = null,
    val metricTarget : String = "",
    val metricUnit : String = "",
    val isMetricEnabled: Boolean = false,
    val imageUri: Uri? = null

)

