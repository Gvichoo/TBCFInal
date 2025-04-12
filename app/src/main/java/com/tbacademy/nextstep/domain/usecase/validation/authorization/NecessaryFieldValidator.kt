package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface NecessaryFieldValidator {
    operator fun invoke(input: String): InputValidationResult
}

class NecessaryFieldValidatorImpl @Inject constructor() : NecessaryFieldValidator {
    override fun invoke(input: String): InputValidationResult {
        return if (input.isEmpty()) {
            InputValidationResult.Failure(error = InputValidationError.Empty)
        } else {
            InputValidationResult.Success
        }
    }
}