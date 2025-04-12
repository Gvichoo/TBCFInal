package com.tbacademy.nextstep.presentation.screen.authentication.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.data.dataStore.PreferenceKey.KEY_REMEMBER_ME
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.usecase.login.LoginUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.EmailValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.NecessaryFieldValidator
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.authentication.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.authentication.login.event.LoginEvent
import com.tbacademy.nextstep.presentation.screen.authentication.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val emailValidator: EmailValidator,
    private val necessaryFieldValidator: NecessaryFieldValidator,
    private val saveValueToLocalStorageUseCase: SaveValueToLocalStorageUseCase,

    ) : BaseViewModel<LoginState, LoginEvent, LoginEffect>(
    initialState = LoginState(),
) {

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> onEmailChanged(email = event.email)
            is LoginEvent.PasswordChanged -> onPasswordChanged(password = event.password)
            is LoginEvent.RememberMeChanged -> onRememberMeChanged(rememberMe = event.rememberMe)
            is LoginEvent.Submit -> submitLogInForm()
            is LoginEvent.RegisterButtonClicked -> viewModelScope.launch {
                emitEffect(LoginEffect.NavToRegisterFragment)
            }
        }
    }

    // On Email Update
    private fun onEmailChanged(email: String) {
        updateState { this.copy(email = email) }

        val emailValidationResult = validateInputOnChange { emailValidator(email = email) }
        val emailErrorMessage: Int? = emailValidationResult?.getErrorMessageResId()

        updateState { this.copy(emailErrorMessage = emailErrorMessage) }
    }

    // On Password Update
    private fun onPasswordChanged(password: String) {
        updateState { this.copy(password = password) }

        val passwordValidationResult =
            validateInputOnChange { necessaryFieldValidator(input = password) }
        val passwordErrorMessage: Int? = passwordValidationResult?.getErrorMessageResId()

        updateState { this.copy(passwordErrorMessage = passwordErrorMessage) }
    }

    // On Remember Me Update
    private fun onRememberMeChanged(rememberMe: Boolean) {
        updateState { this.copy(rememberMe = rememberMe) }

        viewModelScope.launch {
            saveValueToLocalStorageUseCase(KEY_REMEMBER_ME, rememberMe)
        }
    }

    // On Submit
    private fun submitLogInForm() {

        val formIsValid = validateForm(
            email = state.value.email,
            password = state.value.password
        )

        if (formIsValid) {
            loginUser(
                email = state.value.email,
                password = state.value.password,
                rememberMe = state.value.rememberMe
            )
        } else {
            updateState { this.copy(formBeenSubmitted = true) }
        }

    }

    // Form Validation
    private fun validateForm(
        email: String,
        password: String
    ): Boolean {

        // Validate Inputs
        val emailValidationError = emailValidator(email = email).getErrorMessageResId()
        val passwordValidationError =
            necessaryFieldValidator(input = password).getErrorMessageResId()

        // Update states of errors
        updateState {
            copy(
                emailErrorMessage = emailValidationError,
                passwordErrorMessage = passwordValidationError
            )
        }

        // Check if form is valid
        val errors: List<Int?> = listOf(
            emailValidationError,
            passwordValidationError
        )

        return errors.all { it == null }
    }

    private fun loginUser(email: String, password: String,rememberMe: Boolean) {
        viewModelScope.launch {
            loginUseCase(email, password)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            Log.d("LOG_IN_ERROR", "${result.error}")
                            emitEffect(LoginEffect.ShowError(result.error.toMessageRes()))
                        }

                        is Resource.Loading -> {
                            updateState { copy(isLoading = result.loading) }
                        }

                        is Resource.Success -> {
                            withContext(NonCancellable){
                                saveValueToLocalStorageUseCase(KEY_REMEMBER_ME,rememberMe)
                            }
                            updateState { copy(isSuccess = true) }
                            emitEffect(LoginEffect.NavToMainFragment)
                        }
                    }
                }
        }
    }

    // Helpers
    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.formBeenSubmitted)
            validator()
        else null
    }
}