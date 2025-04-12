package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.data.enumClass.MetricUnit
import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface MetricUnitValidator {
    operator fun invoke(metricUnit: String): InputValidationResult
}

class MetricUnitValidatorImpl @Inject constructor() : MetricUnitValidator {
    override fun invoke(metricUnit: String): InputValidationResult {
        return when {
            metricUnit.isBlank() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            MetricUnit.from(metricUnit) == null -> InputValidationResult.Failure(error = InputValidationError.Invalid)
            else -> InputValidationResult.Success
        }
    }
}