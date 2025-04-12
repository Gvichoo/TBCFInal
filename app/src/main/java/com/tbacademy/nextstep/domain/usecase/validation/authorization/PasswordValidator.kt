package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import java.util.regex.Pattern
import javax.inject.Inject

interface PasswordValidator {
    operator fun invoke(password: String): InputValidationResult
}

class PasswordValidatorImpl @Inject constructor() : PasswordValidator {
    private val passwordRegex = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).+$")

    override operator fun invoke(password: String): InputValidationResult {
        return when {
            password.isEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            password.length < 8 -> InputValidationResult.Failure(error = InputValidationError.PasswordShort)
            !passwordRegex.matcher(password)
                .matches() -> InputValidationResult.Failure(error = InputValidationError.PasswordWeak)

            else -> InputValidationResult.Success
        }
    }
}