package com.tbacademy.nextstep.presentation.screen.splash

sealed interface SplashEffect {
    data object NavigateToLogin: SplashEffect
    data object NavigateToMain: SplashEffect
}