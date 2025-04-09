package com.tbacademy.nextstep.presentation.screen.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearValueFromLocalStorageUseCase : ClearValueFromLocalStorageUseCase,
) :ViewModel()  {

    fun logOut() {
        viewModelScope.launch {
            clearValueFromLocalStorageUseCase()
        }
    }


}