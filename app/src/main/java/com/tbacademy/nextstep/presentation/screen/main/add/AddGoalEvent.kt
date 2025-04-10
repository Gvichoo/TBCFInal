package com.tbacademy.nextstep.presentation.screen.main.add

import java.util.Date

sealed interface AddGoalEvent {
    data class CreateGoal(val title: String, val description: String, val goalDate: Date): AddGoalEvent
}