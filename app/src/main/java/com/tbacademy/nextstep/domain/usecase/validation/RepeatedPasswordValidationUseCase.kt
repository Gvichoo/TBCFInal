package com.tbacademy.nextstep.domain.usecase.validation

import com.tbacademy.nextstep.domain.core.ValidationErrors
import com.tbacademy.nextstep.domain.core.ValidationResult

class RepeatedPasswordValidationUseCase {

    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {

        return when{
            password != repeatedPassword ->
                ValidationResult.Failure(ValidationErrors.PASSWORDS_DO_NOT_MATCH)

            else -> {
                ValidationResult.Success
            }
        }
    }
}