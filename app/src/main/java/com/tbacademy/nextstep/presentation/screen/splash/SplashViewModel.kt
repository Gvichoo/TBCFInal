package com.tbacademy.nextstep.presentation.screen.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.data.dataStore.PreferenceKey
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCase
import com.tbacademy.nextstep.presentation.screen.splash.effect.SplashEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val readValueFromLocalStorageUseCase: ReadValueFromLocalStorageUseCase
) : ViewModel() {

    private val _effect = Channel<SplashEffect>()
    val effect: Flow<SplashEffect> get() = _effect.receiveAsFlow()

    init {
        readSession()
    }

    private fun readSession() {
        viewModelScope.launch {
            val rememberMe: Boolean =
                readValueFromLocalStorageUseCase(
                    key = PreferenceKey.KEY_REMEMBER_ME,
                    value = false
                ).first()
            Log.d("Splashss", "Remember me flag is: $rememberMe")

            if (rememberMe)
                _effect.send(SplashEffect.NavigateToMain)
            else
                _effect.send(SplashEffect.NavigateToLogin)
        }
    }
}