package com.tbacademy.nextstep.domain.core

sealed class InputValidationResult {
    data object Success : InputValidationResult()
    data class Failure(val error: InputValidationError) : InputValidationResult()
}

fun InputValidationResult.getErrorMessageResId(): Int? {
    return when (this) {
        is InputValidationResult.Failure -> this.error.errorMessageResId
        else -> null
    }
}