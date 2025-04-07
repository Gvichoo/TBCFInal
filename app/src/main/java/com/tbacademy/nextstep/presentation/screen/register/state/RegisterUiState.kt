package com.tbacademy.nextstep.presentation.screen.register.state

import com.tbacademy.nextstep.domain.core.InputValidationError

data class RegisterUiState(
    val emailError: InputValidationError? = null,
    val passwordError: InputValidationError? = null,
    val repeatedPasswordError: InputValidationError? = null,
    val formBeenSubmitted: Boolean = false,
) {
}