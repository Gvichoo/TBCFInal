package com.tbacademy.nextstep.domain.usecase.validation.authorization

import com.tbacademy.nextstep.domain.core.InputValidationError
import com.tbacademy.nextstep.domain.core.InputValidationResult
import java.util.regex.Pattern.compile
import javax.inject.Inject

interface EmailValidator {
    operator fun invoke(email: String?): InputValidationResult
}

class EmailValidatorImpl @Inject constructor(): EmailValidator {
    private val emailRegex = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override operator fun invoke(email: String?): InputValidationResult {
        return when {
            email.isNullOrEmpty() -> InputValidationResult.Failure(error = InputValidationError.Empty)
            !emailRegex.matcher(email).matches() -> InputValidationResult.Failure(error = InputValidationError.InvalidEmailFormat)
            else -> InputValidationResult.Success
        }
    }
}