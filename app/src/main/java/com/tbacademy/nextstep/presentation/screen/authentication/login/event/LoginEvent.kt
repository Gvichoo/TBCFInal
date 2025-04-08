package com.tbacademy.nextstep.presentation.screen.authentication.login.event


sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data class RememberMeChanged(val rememberMe: Boolean) : LoginEvent

    data object Submit: LoginEvent

    data object RegisterButtonClicked : LoginEvent
}