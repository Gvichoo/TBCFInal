package com.tbacademy.nextstep.presentation.screen.login.effect

sealed interface LoginEffect {


    data object NavToMainFragment : LoginEffect

    data object NavToRegisterFragment : LoginEffect

    data class ShowError(val message: String) : LoginEffect



}