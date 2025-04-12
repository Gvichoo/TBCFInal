package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import javax.inject.Inject

interface RepeatedPasswordValidator {
    operator fun invoke(password: String, repeatedPassword: String): InputValidationResult
}

class RepeatedPasswordValidatorImpl @Inject constructor(): RepeatedPasswordValidator {

    override operator fun invoke(password: String, repeatedPassword: String): InputValidationResult {

        return when{
            repeatedPassword.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            password != repeatedPassword -> InputValidationResult.Failure(error = InputValidationError.PasswordsDoNotMatch)
            else -> InputValidationResult.Success
        }
    }
}