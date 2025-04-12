package com.tbacademy.nextstep.domain.usecase.validation.addGoal

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface AddGoalTitleValidator {
    operator fun invoke(title: String): InputValidationResult
}

class AddGoalTitleValidatorImpl @Inject constructor() : AddGoalTitleValidator{
    override fun invoke(title: String): InputValidationResult {
        return when{
            title.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)

            else -> {
                InputValidationResult.Success
            }
        }
    }

}