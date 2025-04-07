package com.tbacademy.nextstep.presentation.screen.authentication.login.event


sealed class LoginEvent {
    data class LoginButtonClicked(val email: String, val password: String,) : LoginEvent()
    data object RegisterButtonClicked : LoginEvent()

}