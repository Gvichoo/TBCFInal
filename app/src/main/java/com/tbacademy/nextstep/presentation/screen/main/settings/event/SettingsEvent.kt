package com.tbacademy.nextstep.presentation.screen.main.settings.event

sealed interface SettingsEvent {
    data object Logout: SettingsEvent
}