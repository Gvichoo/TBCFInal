package com.tbacademy.nextstep.presentation.screen.main.add

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDateValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDescriptionValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalTitleValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricTargetValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricUnitValidator
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.main.add.effect.AddGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.add.state.AddGoalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase,
    private val titleValidator : AddGoalTitleValidator,
    private val descriptionValidator : AddGoalDescriptionValidator,
    private val goalDateValidator: AddGoalDateValidator,
    private val metricTargetValidator : MetricTargetValidator,
    private val metricUnitValidator: MetricUnitValidator

) : BaseViewModel<AddGoalState,AddGoalEvent,AddGoalEffect>(
    initialState = AddGoalState(),
) {


    override fun onEvent(event: AddGoalEvent) {
        when (event) {
            is AddGoalEvent.CreateGoal -> createGoal(
                title = event.title,
                description = event.description,
                targetDate = event.goalDate,
                metricTarget = event.metricTarget,
                metricUnit = event.metricUnit,
                isMetricEnabled = event.isMetricEnabled
            )
            is AddGoalEvent.GoalDescriptionChanged -> onDescriptionChanged(description = event.description)
            is AddGoalEvent.GoalTitleChanged ->  onTitleChanged(title = event.title)

            AddGoalEvent.OnCreateGoalBtnClicked -> viewModelScope.launch { emitEffect(AddGoalEffect.NavToHomeFragment) }
            AddGoalEvent.Submit -> submitAddGoalForm()
            is AddGoalEvent.GoalDateChanged ->  onDateChanged(date = event.date)
            is AddGoalEvent.MetricToggle -> updateState { this.copy(isMetricEnabled = event.enabled) }
            is AddGoalEvent.GoalMetricTargetChanged -> onMetricTargetChanged(metricTarget = event.metricTarget)
            is AddGoalEvent.GoalMetricUnitChanged -> onMetricUnitChanged(metricUnit = event.metricUnit)
        }
    }


    //On Metric Target Update
    private fun onMetricTargetChanged(metricTarget : String){
        updateState { this.copy(metricTarget = metricTarget) }

        val metricTargetValidationResult = validateInputOnChange { metricTargetValidator(metricTarget = metricTarget) }
        val metricTargetErrorMessage : Int? = metricTargetValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalMetricTargetErrorMessage = metricTargetErrorMessage) }
    }

    //On Metric Unit Update
    private fun onMetricUnitChanged(metricUnit : String ){
        updateState { this.copy(metricUnit = metricUnit) }

        val metricUnitValidationResult = validateInputOnChange { metricUnitValidator(metricUnit = metricUnit) }
        val metricUnitErrorMessage: Int? = metricUnitValidationResult?.getErrorMessageResId()

        updateState { this.copy(goalMetricUnitErrorMessage = metricUnitErrorMessage) }
    }

    // On Title Update
    private fun onTitleChanged(title: String) {
        updateState { this.copy(title = title) }

        val titleValidationResult = validateInputOnChange { titleValidator(title = title) }
        val titleErrorMessage: Int? = titleValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalTitleErrorMessage = titleErrorMessage) }
    }

    // On Description Update
    private fun onDescriptionChanged(description: String){
        updateState { this.copy(description = description) }

        val descriptionValidationResult = validateInputOnChange { descriptionValidator(description = description) }
        val descriptionErrorMessage: Int? = descriptionValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDescriptionErrorMessage = descriptionErrorMessage) }
    }

    private fun onDateChanged(date: Date){
        updateState { this.copy(goalDate = date) }

        val dateValidationResult = validateInputOnChange { goalDateValidator(date = date) }
        val dateErrorMessage : Int? = dateValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDateErrorMessage = dateErrorMessage) }
    }

    // On Submit
    private fun submitAddGoalForm() {

        val formIsValid = validateForm(
            title = state.value.title,
            description = state.value.description,
            goalDate = state.value.goalDate,
            metricUnit = state.value.metricUnit,
            metricTarget = state.value.metricTarget,
            isMetricEnabled = state.value.isMetricEnabled
        )

        if (formIsValid) {
            state.value.goalDate?.let {
                createGoal(
                    title = state.value.title,
                    description = state.value.description,
                    targetDate = it,
                    metricUnit = state.value.metricUnit,
                    metricTarget = state.value.metricTarget,
                    isMetricEnabled = state.value.isMetricEnabled
                )
            }
        }else {
            updateState { this.copy(formBeenSubmitted = true) }
        }
        Log.d("SUBMIT_FORM", "Title: ${state.value.title}, Desc: ${state.value.description}, Date: ${state.value.goalDate}")
    }

    private fun validateForm(
        title: String,
        description: String,
        goalDate : Date?,
        metricUnit: String,
        metricTarget: String,
        isMetricEnabled: Boolean

    ): Boolean {

        // Validate Inputs
        val titleValidationError = titleValidator(title = title).getErrorMessageResId()
        val descriptionValidationForm = descriptionValidator(description = description).getErrorMessageResId()
        val dateValidationError = goalDateValidator(date = goalDate).getErrorMessageResId()

        var metricUnitError: Int? = null
        var metricTargetError: Int? = null

        if (isMetricEnabled) {
            metricUnitError = metricUnitValidator(metricUnit = metricUnit).getErrorMessageResId()
            metricTargetError = metricTargetValidator(metricTarget = metricTarget).getErrorMessageResId()
        }

        // Update states of errors
        updateState {
            copy(
                goalTitleErrorMessage = titleValidationError,
                goalDescriptionErrorMessage = descriptionValidationForm,
                goalDateErrorMessage = dateValidationError,
                goalMetricUnitErrorMessage = metricUnitError,
                goalMetricTargetErrorMessage = metricTargetError
            )
        }


        // Check if form if valid
        val errors: List<Int?> = listOf(
            titleValidationError,
            descriptionValidationForm,
            dateValidationError,
            if (isMetricEnabled) metricUnitError else null,
            if (isMetricEnabled) metricTargetError else null
        )

        return errors.all { it == null }
    }

    // Helpers
    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.formBeenSubmitted)
            validator()
        else null
    }


    private fun createGoal(
        title: String,
        description: String,
        targetDate: Date,
        metricUnit: String,
        metricTarget: String,
        isMetricEnabled: Boolean
    ) {
        viewModelScope.launch {
            val newGoal = Goal(
                title = title,
                description = description,
                targetDate = targetDate,
                metricUnit = if (isMetricEnabled) metricUnit else null,
                metricTarget = if (isMetricEnabled) metricTarget else null
            )
            createGoalUseCase(

                goal = newGoal
            ).collect { result ->
                when(result){

                    is Resource.Error ->
                        emitEffect(AddGoalEffect.ShowError(result.error.toMessageRes()))

                    is Resource.Loading ->
                        updateState { copy(isLoading = result.loading) }

                    is Resource.Success ->{
                        updateState { copy(isSuccess = true) }
                        emitEffect(AddGoalEffect.NavToHomeFragment)
                    }
                }
            }
            Log.d("CREATE_GOAL", "GOAL: $newGoal")
        }
    }
}