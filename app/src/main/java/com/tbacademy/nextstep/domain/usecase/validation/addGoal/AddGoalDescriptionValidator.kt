package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface AddGoalDescriptionValidator {
    operator fun invoke(description: String): InputValidationResult
}


class AddGoalDescriptionValidatorImpl @Inject constructor() : AddGoalDescriptionValidator{
    override fun invoke(description: String): InputValidationResult {
        return when{
            description.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)

            else -> {
                InputValidationResult.Success
            }
        }
    }

}

