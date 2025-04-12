package com.tbacademy.nextstep.presentation.screen.main.add.state

import java.util.Date

data class AddGoalState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val formBeenSubmitted: Boolean = false,

    val goalTitleErrorMessage : Int? = null,
    val goalDescriptionErrorMessage: Int? = null,
    val goalDateErrorMessage: Int? = null,
    val goalMetricTargetErrorMessage : Int? = null,
    val goalMetricUnitErrorMessage : Int? = null,

    //Ui State
    val title : String = "",
    val description: String = "",
    val goalDate : Date? = null,
    val metricTarget : String = "",
    val metricUnit : String = "",
    val isMetricEnabled: Boolean = false,
){
    val isCreateGoalEnabled: Boolean
        get() = goalTitleErrorMessage == null && goalDescriptionErrorMessage == null
}