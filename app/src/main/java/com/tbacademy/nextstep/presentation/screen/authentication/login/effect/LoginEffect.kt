package com.tbacademy.nextstep.presentation.screen.authentication.login.effect

sealed interface LoginEffect {
    data object NavToMainFragment : LoginEffect
    data object NavToRegisterFragment : LoginEffect
    data class ShowError(val message: Int) : LoginEffect
}