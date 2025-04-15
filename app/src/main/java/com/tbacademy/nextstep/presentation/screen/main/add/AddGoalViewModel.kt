package com.tbacademy.nextstep.presentation.screen.main.add

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDateUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricTargetUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricUnitUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.main.add.effect.AddGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.add.state.AddGoalState
import com.tbacademy.nextstep.presentation.screen.main.add.state.AddGoalUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase,
    private val validateTitleUseCase: ValidateAddGoalTitleUseCase,
    private val validateDescriptionUseCase: ValidateAddGoalDescriptionUseCase,
    private val validateDateUseCase: ValidateAddGoalDateUseCase,
    private val validateMetricTargetUseCase: ValidateMetricTargetUseCase,
    private val validateMetricUnitUseCase: ValidateMetricUnitUseCase

) : BaseViewModel<AddGoalState, AddGoalEvent, AddGoalEffect, AddGoalUiState>(
    initialState = AddGoalState(),
    initialUiState = AddGoalUiState()
) {


    override fun onEvent(event: AddGoalEvent) {
        when (event) {
            is AddGoalEvent.CreateGoal -> createGoal(
                title = event.title,
                description = event.description,
                targetDate = event.goalDate,
                metricTarget = event.metricTarget,
                metricUnit = event.metricUnit,
                isMetricEnabled = event.isMetricEnabled,
                imageUri = event.imageUrl
            )

            is AddGoalEvent.GoalDescriptionChanged -> onDescriptionChanged(description = event.description)
            is AddGoalEvent.GoalTitleChanged -> onTitleChanged(title = event.title)

            AddGoalEvent.OnCreateGoalBtnClicked -> viewModelScope.launch { emitEffect(AddGoalEffect.NavToHomeFragment) }
            AddGoalEvent.Submit -> submitAddGoalForm()
            is AddGoalEvent.GoalDateChanged -> onDateChanged(date = event.date)
            is AddGoalEvent.MetricToggle -> updateUiState { this.copy(isMetricEnabled = event.enabled) }
            is AddGoalEvent.GoalMetricTargetChanged -> onMetricTargetChanged(metricTarget = event.metricTarget)
            is AddGoalEvent.GoalMetricUnitChanged -> onMetricUnitChanged(metricUnit = event.metricUnit)

            is AddGoalEvent.ImageSelected -> updateUiState { this.copy(imageUri = event.imageUri) }
            AddGoalEvent.PickImageClicked -> viewModelScope.launch { emitEffect(AddGoalEffect.LaunchMediaPicker) }
            AddGoalEvent.ImageCleared -> updateUiState { this.copy(imageUri = null) }
        }
    }

    private fun createGoal(
        title: String,
        description: String,
        targetDate: Date,
        metricUnit: String,
        metricTarget: String,
        isMetricEnabled: Boolean,
        imageUri: Uri?
    ) {
        viewModelScope.launch {


            val newGoal = Goal(
                title = title,
                description = description,
                targetDate = targetDate,
                metricUnit = if (isMetricEnabled) metricUnit else null,
                metricTarget = if (isMetricEnabled) metricTarget else null,
                imageUri = imageUri
            )
            createGoalUseCase(

                goal = newGoal,
            ).collect { result ->
                when (result) {

                    is Resource.Error ->
                        emitEffect(AddGoalEffect.ShowError(result.error.toMessageRes()))

                    is Resource.Loading ->
                        updateState { copy(isLoading = result.loading) }

                    is Resource.Success -> {
                        updateState { copy(isSuccess = true) }
                        emitEffect(AddGoalEffect.NavToHomeFragment)
                    }
                }
            }
            Log.d("CREATE_GOAL", "GOAL: $newGoal")
        }
    }


    //On Metric Target Update
    private fun onMetricTargetChanged(metricTarget: String) {
        updateUiState { this.copy(metricTarget = metricTarget) }

        val metricTargetValidationResult =
            validateInputOnChange { validateMetricTargetUseCase(metricTarget = metricTarget) }
        val metricTargetErrorMessage: Int? = metricTargetValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalMetricTargetErrorMessage = metricTargetErrorMessage) }
    }

    //On Metric Unit Update
    private fun onMetricUnitChanged(metricUnit: String) {
        updateUiState { this.copy(metricUnit = metricUnit) }

        val metricUnitValidationResult =
            validateInputOnChange { validateMetricUnitUseCase(metricUnit = metricUnit) }
        val metricUnitErrorMessage: Int? = metricUnitValidationResult?.getErrorMessageResId()

        updateState { this.copy(goalMetricUnitErrorMessage = metricUnitErrorMessage) }
    }

    // On Title Update
    private fun onTitleChanged(title: String) {
        updateUiState { this.copy(title = title) }

        val titleValidationResult = validateInputOnChange { validateTitleUseCase(title = title) }
        val titleErrorMessage: Int? = titleValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalTitleErrorMessage = titleErrorMessage) }
    }

    // On Description Update
    private fun onDescriptionChanged(description: String) {
        updateUiState { this.copy(description = description) }

        val descriptionValidationResult =
            validateInputOnChange { validateDescriptionUseCase(description = description) }
        val descriptionErrorMessage: Int? = descriptionValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDescriptionErrorMessage = descriptionErrorMessage) }
    }

    private fun onDateChanged(date: Date) {
        updateUiState { this.copy(goalDate = date) }

        val dateValidationResult = validateInputOnChange { validateDateUseCase(date = date) }
        val dateErrorMessage: Int? = dateValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDateErrorMessage = dateErrorMessage) }
    }

    // On Submit
    private fun submitAddGoalForm() {

        val formIsValid = validateForm(
            title = uiState.value.title,
            description = uiState.value.description,
            goalDate = uiState.value.goalDate,
            metricUnit = uiState.value.metricUnit,
            metricTarget = uiState.value.metricTarget,
            isMetricEnabled = uiState.value.isMetricEnabled,
            imageUri = uiState.value.imageUri

        )

        if (formIsValid) {
            uiState.value.goalDate?.let {
                uiState.value.imageUri?.let { it1 ->
                    createGoal(
                        title = uiState.value.title,
                        description = uiState.value.description,
                        targetDate = it,
                        metricUnit = uiState.value.metricUnit,
                        metricTarget = uiState.value.metricTarget,
                        isMetricEnabled = uiState.value.isMetricEnabled,
                        imageUri = uiState.value.imageUri
                    )
                }
            }
        } else {
            updateState { this.copy(formBeenSubmitted = true) }
        }
        Log.d(
            "SUBMIT_FORM",
            "Title: ${uiState.value.title}, Desc: ${uiState.value.description}, Date: ${uiState.value.goalDate}"
        )
    }


    private fun validateForm(
        title: String,
        description: String,
        goalDate: Date?,
        metricUnit: String,
        metricTarget: String,
        isMetricEnabled: Boolean,
        imageUri: Uri?


    ): Boolean {

        // Validate Inputs
        val titleValidationError = validateTitleUseCase(title = title).getErrorMessageResId()
        val descriptionValidationForm =
            validateDescriptionUseCase(description = description).getErrorMessageResId()
        val dateValidationError = validateDateUseCase(date = goalDate).getErrorMessageResId()

        var metricUnitError: Int? = null
        var metricTargetError: Int? = null

        if (isMetricEnabled) {
            metricUnitError =
                validateMetricUnitUseCase(metricUnit = metricUnit).getErrorMessageResId()
            metricTargetError =
                validateMetricTargetUseCase(metricTarget = metricTarget).getErrorMessageResId()
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
}