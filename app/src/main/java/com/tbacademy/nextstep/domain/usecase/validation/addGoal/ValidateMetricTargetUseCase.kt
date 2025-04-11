package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface ValidateMetricTargetUseCase {
    operator fun invoke(metricTarget : Int) : InputValidationResult
}

class ValidateMetricTargetUseCaseImpl @Inject constructor() : ValidateMetricTargetUseCase{
    override fun invoke(metricTarget: Int): InputValidationResult {
        return when {
            metricTarget <= 0 -> InputValidationResult.Failure(error = InputValidationError.Empty)

            else -> InputValidationResult.Success
        }
    }
}