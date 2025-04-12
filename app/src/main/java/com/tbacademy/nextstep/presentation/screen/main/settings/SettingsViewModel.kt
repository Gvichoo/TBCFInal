package com.tbacademy.nextstep.presentation.screen.main.settings

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearValueFromLocalStorageUseCase: ClearValueFromLocalStorageUseCase,
) : BaseViewModel<Unit, SettingsEvent, SettingsEffect>(
    initialState = Unit,
) {

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Logout -> logout()
        }
    }

    // On Logout
    private fun logout() {
        viewModelScope.launch {
            clearValueFromLocalStorageUseCase()
            emitEffect(effect = SettingsEffect.NavigateToLogin)
        }
    }
}