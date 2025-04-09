package com.tbacademy.nextstep.presentation.screen.splash.effect

sealed interface SplashEffect {
    data object NavigateToLogin: SplashEffect
    data object NavigateToMain: SplashEffect
}