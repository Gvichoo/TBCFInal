package com.tbacademy.nextstep.presentation.screen.authentication.register.event

sealed interface RegisterEvent {
    data class UsernameChanged(val username: String) : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class RepeatedPasswordChanged(val repeatedPassword: String) :
        RegisterEvent
    data object PasswordVisibilityToggle: RegisterEvent
    data object RepeatedPasswordVisibilityToggle: RegisterEvent

    data class OnSignUpBtnClicked(
        val nickname: String,
        val email: String,
        val password: String,
        val repeatedPassword: String
    ) : RegisterEvent

    data object Submit: RegisterEvent

    data object OnLogInBtnClicked : RegisterEvent
}