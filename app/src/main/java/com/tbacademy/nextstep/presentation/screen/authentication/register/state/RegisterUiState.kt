package com.tbacademy.nextstep.presentation.screen.authentication.register.state

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
)