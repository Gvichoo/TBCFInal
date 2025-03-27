package com.tbacademy.nextstep.domain.usecase.validation

import com.tbacademy.nextstep.domain.core.ValidationErrors
import com.tbacademy.nextstep.domain.core.ValidationResult
import java.util.regex.Pattern

class EmailValidationUseCase  {
    private val emailRegex = Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    )

    operator fun invoke(email: String?): ValidationResult {
        return when {
            email.isNullOrEmpty() -> ValidationResult.Failure(ValidationErrors.EMPTY)
            !emailRegex.matcher(email).matches() -> ValidationResult.Failure(ValidationErrors.INVALID_FORMAT)
            else -> ValidationResult.Success
        }
    }
}