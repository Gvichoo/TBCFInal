package com.tbacademy.nextstep.domain.core

enum class ValidationErrors(val message: String) {
    EMPTY("Inputs are Empty!"),
    INVALID_FORMAT("Invalid email format!"),
    INVALID_PASSWORD("Password should be at least 8 characters!"),
    INVALID_NICKNAME_SHORT("Nickname must be at least 4 characters"),
    INVALID_NICKNAME_LONG("Nickname must be no longer than 20 characters"),
    PASSWORD_WEAK("Password must contain at least one letter and one digit!"),
    PASSWORDS_DO_NOT_MATCH("Passwords do not match!"),
    NICKNAME_REGEX_VALIDATION("Nickname can only contain letters, numbers, and underscores")
}