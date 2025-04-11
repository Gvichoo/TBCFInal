package com.tbacademy.nextstep.presentation.screen.main.add.event

import java.util.Date

sealed interface AddGoalEvent {
    data class GoalTitleChanged(val title : String) : AddGoalEvent
    data class GoalDescriptionChanged(val description: String) : AddGoalEvent
    data class GoalDateChanged(val date: Date) : AddGoalEvent
    data class GoalMetricTargetChanged(val metricTarget : Int) : AddGoalEvent
    data class GoalMetricUnitChanged(val metricUnit : String) : AddGoalEvent

    data class MetricToggle(val enabled: Boolean) : AddGoalEvent



    data object Submit: AddGoalEvent

    data object OnCreateGoalBtnClicked : AddGoalEvent

    data class CreateGoal(val title: String, val description: String, val goalDate: Date,val metricTarget: Int,val metricUnit : String,val isMetricEnabled : Boolean): AddGoalEvent


}