package com.tbacademy.nextstep.presentation.screen.splash

import androidx.lifecycle.ViewModel
import com.tbacademy.nextstep.data.dataStore.PreferenceKey
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val readValueFromLocalStorageUseCase: ReadValueFromLocalStorageUseCase
) : ViewModel() {

    private val rememberKey = PreferenceKey.KEY_REMEMBER_ME

     suspend fun isRememberMeEnabled(): Boolean {
        return readValueFromLocalStorageUseCase(rememberKey, false).first()
    }


}