package com.tbacademy.nextstep.presentation.screen.authentication.register.effect

sealed interface RegisterEffect {
    data class ShowError(val message : String) : RegisterEffect
    data object NavToLogInFragment : RegisterEffect
}