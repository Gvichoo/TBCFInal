package com.tbacademy.nextstep.domain.repository.goal

import android.net.Uri
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    suspend fun createGoal(goal: Goal): Flow<Resource<Boolean>>
}