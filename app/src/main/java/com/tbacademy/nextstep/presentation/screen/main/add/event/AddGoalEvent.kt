package com.tbacademy.nextstep.presentation.screen.main.add.event

import java.util.Date

sealed interface AddGoalEvent {
    data class GoalTitleChanged(val title : String) : AddGoalEvent
    data class GoalDescriptionChanged(val description: String) : AddGoalEvent

    data object Submit: AddGoalEvent

    data object OnCreateGoalBtnClicked : AddGoalEvent

    data class CreateGoal(val title: String, val description: String, val goalDate: Date): AddGoalEvent


}