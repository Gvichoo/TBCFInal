package com.tbacademy.nextstep.domain.usecase.goal

import android.util.Log
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateGoalUseCase {
    suspend operator fun invoke(goal: Goal): Flow<Resource<Boolean>>
}

class CreateGoalUseCaseImpl @Inject constructor(
    private val goalRepository: GoalRepository
) : CreateGoalUseCase {
    override suspend fun invoke(goal: Goal): Flow<Resource<Boolean>> {
        Log.d("USE_CASE", "Use case invoked")
        return goalRepository.createGoal(goal)
    }
}