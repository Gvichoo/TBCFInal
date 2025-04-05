package com.tbacademy.nextstep.presentation.screen.login

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.ValidationResult
import com.tbacademy.nextstep.domain.usecase.login.LoginUseCase
import com.tbacademy.nextstep.domain.usecase.validation.EmailValidationUseCase
import com.tbacademy.nextstep.domain.usecase.validation.PasswordValidationUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.login.event.LoginEvent
import com.tbacademy.nextstep.presentation.screen.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val emailValidationUseCase: EmailValidationUseCase,
    private val passwordValidationUseCase: PasswordValidationUseCase
) : BaseViewModel<LoginState,LoginEvent,LoginEffect>(LoginState()) {


    private fun validateInputsAndLogin(email: String, password: String) {

        val emailValidation = emailValidationUseCase(email)
        val passwordValidation = passwordValidationUseCase(password)
        when {
            emailValidation is ValidationResult.Failure -> {
                showError(emailValidation.error.message)
                return
            }
            passwordValidation is ValidationResult.Failure -> {
                showError(passwordValidation.error.message)
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



    override fun obtainEvent(event: LoginEvent) {
        when(event){
            is LoginEvent.LoginButtonClicked -> validateInputsAndLogin(event.email,event.password)
            LoginEvent.RegisterButtonClicked -> viewModelScope.launch {
                emitEffect(LoginEffect.NavToRegisterFragment)
            }
        }
    }
}