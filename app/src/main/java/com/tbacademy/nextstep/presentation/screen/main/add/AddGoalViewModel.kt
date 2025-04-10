package com.tbacademy.nextstep.presentation.screen.main.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase
) : ViewModel() {

    fun onEvent(event: AddGoalEvent) {
        when (event) {
            is AddGoalEvent.CreateGoal -> createGoal(
                title = event.title,
                description = event.description,
                targetDate = event.goalDate
            )
        }
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
            ).collect {

            }
            Log.d("CREATE_GOAL", "GOAL: $newGoal")
        }
    }
}