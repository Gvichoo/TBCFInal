package com.tbacademy.nextstep.domain.core

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Failure(val error: ValidationErrors) : ValidationResult()
}