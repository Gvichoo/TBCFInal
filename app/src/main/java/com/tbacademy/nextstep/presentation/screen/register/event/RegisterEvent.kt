package com.tbacademy.nextstep.presentation.screen.register.event

sealed class RegisterEvent {
    data class SignUpButtonClicked(val nickname : String ,val email: String, val password: String, val repeatedPassword: String) : RegisterEvent()
    data object LogInButtonClicked : RegisterEvent()
}