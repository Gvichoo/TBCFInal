package com.tbacademy.nextstep.domain.usecase.validation

import com.tbacademy.nextstep.domain.core.ValidationErrors
import com.tbacademy.nextstep.domain.core.ValidationResult

class NicknameValidationUseCase {

    operator fun invoke(nickname: String): ValidationResult {

        return when{
            nickname.isEmpty() -> ValidationResult.Failure(ValidationErrors.EMPTY)
            nickname.length < 4 -> ValidationResult.Failure(ValidationErrors.INVALID_NICKNAME_SHORT)
            nickname.length > 20 -> ValidationResult.Failure(ValidationErrors.INVALID_NICKNAME_LONG)
            !nickname.matches(("^[A-Za-z0-9_]+$".toRegex())) -> {
                ValidationResult.Failure(ValidationErrors.NICKNAME_REGEX_VALIDATION)
            }
            else -> {
                ValidationResult.Success
            }
        }

    }
}