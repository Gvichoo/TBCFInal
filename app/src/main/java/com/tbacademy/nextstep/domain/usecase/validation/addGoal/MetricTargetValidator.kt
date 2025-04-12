package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface MetricTargetValidator {
    operator fun invoke(metricTarget : String) : InputValidationResult
}

class MetricTargetValidatorImpl @Inject constructor() : MetricTargetValidator{
    override fun invoke(metricTarget: String): InputValidationResult {
        return when {
            metricTarget.isBlank() -> InputValidationResult.Failure(InputValidationError.Empty)

            !metricTarget.matches(Regex("\\d+")) -> InputValidationResult.Failure(InputValidationError.Invalid)

            else -> {
                val value = metricTarget.toInt()
                when {
                    value <= 0 -> InputValidationResult.Failure(InputValidationError.Invalid)
                    else -> InputValidationResult.Success
                }
            }
        }
    }
}