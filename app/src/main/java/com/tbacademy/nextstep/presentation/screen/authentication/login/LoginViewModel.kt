package com.tbacademy.nextstep.presentation.screen.authentication.login

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.usecase.login.LoginUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateEmailUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.ValidatePasswordUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.authentication.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.authentication.login.event.LoginEvent
import com.tbacademy.nextstep.presentation.screen.authentication.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val emailValidationUseCase: ValidateEmailUseCaseImpl,
    private val passwordValidationUseCase: ValidatePasswordUseCase
) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(LoginState()) {


    override fun onEvent(event: LoginEvent) {
        when(event){
            is LoginEvent.LoginButtonClicked -> validateInputsAndLogin(event.email,event.password)
            LoginEvent.RegisterButtonClicked -> viewModelScope.launch {
                emitEffect(LoginEffect.NavToRegisterFragment)
            }
        }
    }

    private fun validateInputsAndLogin(email: String, password: String) {

        val emailValidation = emailValidationUseCase(email)
        val passwordValidation = passwordValidationUseCase(password)
        when {
            emailValidation is InputValidationResult.Failure -> {
                return
            }
            passwordValidation is InputValidationResult.Failure -> {
                return
            }
            else -> {
                loginUser(email, password)
            }
        }

    }

    private fun showError(errorMessage: String) {
        viewModelScope.launch {
            emitEffect(LoginEffect.ShowError(errorMessage))
        }
    }


    private fun loginUser(email: String, password: String) {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            loginUseCase(email, password)
                .collect { result ->
                    when (result) {

                        is Resource.Error -> {
                            emitEffect(LoginEffect.ShowError(result.errorMessage))
                            updateState { copy(isLoading = false) }
                        }
                        is Resource.Loading ->{
                            updateState { copy(isLoading = result.loading) }

                        }
                        is Resource.Success -> {
                            updateState { copy(isSuccess = true,isLoading = false) }
                            emitEffect(LoginEffect.NavToMainFragment)
                        }
                    }
                }
        }
    }
}