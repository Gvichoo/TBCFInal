package com.tbacademy.nextstep.presentation.screen.register

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.usecase.register.RegisterUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateUsernameUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidatePasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateRepeatedPasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateEmailUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.register.effect.RegisterEffect
import com.tbacademy.nextstep.presentation.screen.register.event.RegisterEvent
import com.tbacademy.nextstep.presentation.screen.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val nicknameValidationUseCase: ValidateUsernameUseCase,
    private val registerUseCase: RegisterUseCase,
    private val emailValidationUseCase: ValidateEmailUseCase,
    private val passwordValidationUseCase: ValidatePasswordUseCase,
    private val repeatedPasswordValidationUseCase: ValidateRepeatedPasswordUseCase
) : BaseViewModel<RegisterState, RegisterEvent, RegisterEffect>(RegisterState()) {


    private fun validateInputsAndRegister(nickname: String, email: String, password: String, passwordRepeated: String) {

        val nicknameValidation = nicknameValidationUseCase(nickname)
        val emailValidation = emailValidationUseCase(email)
        val passwordValidation = passwordValidationUseCase(password)
        val repeatedPasswordValidation = repeatedPasswordValidationUseCase(password, passwordRepeated)
        when {
            emailValidation is InputValidationResult.Failure -> {
                return
            }

            passwordValidation is InputValidationResult.Failure -> {
                return
            }

            repeatedPasswordValidation is InputValidationResult.Failure -> {
                return
            }

            nicknameValidation is InputValidationResult.Failure -> {
                return
            }

            else -> {
                registerUser(nickname, email, password)
            }
        }
    }

    private fun registerUser(nickname: String, email: String, password: String) {
        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            registerUseCase(nickname, email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        updateState { copy(isLoading = false) }
                        emitEffect(RegisterEffect.ShowError(result.errorMessage))
                    }

                    is Resource.Loading -> {
                        updateState { copy(isLoading = result.loading) }
                    }

                    is Resource.Success -> {
                        updateState { copy(isLoading = false, isSuccess = true) }
                        emitEffect(RegisterEffect.NavToLogInFragment)
                    }
                }
            }
        }
    }


    private fun showError(errorMessage: String) {
        viewModelScope.launch {
            emitEffect(RegisterEffect.ShowError(errorMessage))
        }
    }

    override fun onEvent(event: RegisterEvent) {
        when(event){
            is RegisterEvent.SignUpButtonClicked -> validateInputsAndRegister(event.nickname,event.email,event.password,event.repeatedPassword)
            RegisterEvent.LogInButtonClicked -> viewModelScope.launch {
                emitEffect(RegisterEffect.NavToLogInFragment)
            }
        }
    }
}