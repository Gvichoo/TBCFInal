package com.tbacademy.nextstep.domain.core

import com.tbacademy.nextstep.R

sealed class InputValidationError {
    // General
    data object Empty : InputValidationError()

    // Email
    data object InvalidEmailFormat : InputValidationError()

    // Username
    data object UsernameShort : InputValidationError()
    data object UsernameLong : InputValidationError()
    data object InvalidUsernameFormat : InputValidationError()

    // Password
    data object PasswordShort : InputValidationError()
    data object PasswordWeak : InputValidationError()
    data object PasswordsDoNotMatch : InputValidationError()
}