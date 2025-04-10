package com.tbacademy.nextstep.presentation.screen.main.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.authentication.register.effect.RegisterEffect
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
    private val validateTitleUseCase : ValidateAddGoalTitleUseCase,
    private val validateDescriptionUseCase : ValidateAddGoalDescriptionUseCase

) : BaseViewModel<AddGoalState,AddGoalEvent,AddGoalEffect,AddGoalUiState>(
    initialState = AddGoalState(),
    initialUiState = AddGoalUiState()) {


    override fun onEvent(event: AddGoalEvent) {
        when (event) {
            is AddGoalEvent.CreateGoal -> createGoal(
                title = event.title,
                description = event.description,
                targetDate = event.goalDate
            )
            is AddGoalEvent.GoalDescriptionChanged -> onDescriptionChanged(description = event.description)
            is AddGoalEvent.GoalTitleChanged ->  onTitleChanged(title = event.title)

            AddGoalEvent.OnCreateGoalBtnClicked -> viewModelScope.launch { emitEffect(AddGoalEffect.NavToHomeFragment) }
            AddGoalEvent.Submit -> submitAddGoalForm()
        }
    }

    // On Title Update
    private fun onTitleChanged(title: String) {
        updateUiState { this.copy(title = title) }

        val titleValidationResult = validateInputOnChange { validateTitleUseCase(title = title) }
        val titleErrorMessage: Int? = titleValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalTitleErrorMessage = titleErrorMessage) }
    }

    // On Description Update
    private fun onDescriptionChanged(description: String){
        updateUiState { this.copy(description = description) }

        val descriptionValidationResult = validateInputOnChange { validateDescriptionUseCase(description = description) }
        val descriptionErrorMessage: Int? = descriptionValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDescriptionErrorMessage = descriptionErrorMessage) }
    }


    // On Submit
    private fun submitAddGoalForm() {

        val formIsValid = validateForm(
            title = uiState.value.title,
            description = uiState.value.description,
        )

        if (formIsValid) {
            uiState.value.goalDate?.let {
                createGoal(
                    title = uiState.value.title,
                    description = uiState.value.description,
                    targetDate = it,
                )
            }
        } else {
            updateState { this.copy(formBeenSubmitted = true) }
        }
    }




    private fun validateForm(
        title: String,
        description: String
    ): Boolean {

        // Validate Inputs
        val titleValidationError = validateTitleUseCase(title = title).getErrorMessageResId()
        val descriptionValidationForm = validateDescriptionUseCase(description = description).getErrorMessageResId()

        // Update states of errors
        updateState {
            copy(
                goalTitleErrorMessage = titleValidationError,
                goalDescriptionErrorMessage = descriptionValidationForm,
            )
        }

        // Check if form if valid
        val errors: List<Int?> = listOf(
            titleValidationError,
            descriptionValidationForm
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
        targetDate: Date
    ) {
        viewModelScope.launch {
            val newGoal = Goal(
                title = title,
                description = description,
                targetDate = targetDate
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