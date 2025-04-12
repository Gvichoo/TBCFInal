package com.tbacademy.nextstep.presentation.screen.authentication.register

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.usecase.register.RegisterUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.UsernameValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.PasswordValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.RepeatedPasswordValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.EmailValidator
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.screen.authentication.register.effect.RegisterEffect
import com.tbacademy.nextstep.presentation.screen.authentication.register.event.RegisterEvent
import com.tbacademy.nextstep.presentation.screen.authentication.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usernameValidator: UsernameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val repeatedPasswordValidator: RepeatedPasswordValidator,
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel<RegisterState, RegisterEvent, RegisterEffect>(
    initialState = RegisterState(),
) {
    override fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> onUsernameChanged(username = event.username)
            is RegisterEvent.EmailChanged -> onEmailChanged(email = event.email)
            is RegisterEvent.PasswordChanged -> onPasswordChanged(password = event.password)
            is RegisterEvent.RepeatedPasswordChanged -> onRepeatedPasswordChanged(repeatedPassword = event.repeatedPassword)

            is RegisterEvent.Submit -> submitRegisterForm()

            is RegisterEvent.OnRegisterBtnClicked -> viewModelScope.launch { emitEffect(RegisterEffect.NavToLogInFragment) }
        }
    }

    // On Username Update
    private fun onUsernameChanged(username: String) {
        updateState { this.copy(username = username) }

        val usernameValidationResult =
            validateInputOnChange { usernameValidator(username = username) }
        val usernameErrorMessage: Int? = usernameValidationResult?.getErrorMessageResId()
        updateState { this.copy(usernameErrorMessage = usernameErrorMessage) }
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
            validateInputOnChange { passwordValidator(password = password) }
        val repeatedPasswordValidationResult = validateInputOnChange {
            repeatedPasswordValidator(
                repeatedPassword = state.value.repeatedPassword,
                password = password
            )
        }

        val passwordErrorMessage: Int? = passwordValidationResult?.getErrorMessageResId()
        val repeatedPasswordErrorMessage: Int? =
            repeatedPasswordValidationResult?.getErrorMessageResId()

        updateState {
            this.copy(
                passwordErrorMessage = passwordErrorMessage,
                repeatedPasswordErrorMessage = repeatedPasswordErrorMessage
            )
        }
    }

    // On Repeated Password Update
    private fun onRepeatedPasswordChanged(repeatedPassword: String) {
        updateState { this.copy(repeatedPassword = repeatedPassword) }

        val repeatedPasswordValidationResult = validateInputOnChange {
            repeatedPasswordValidator(
                repeatedPassword = repeatedPassword,
                password = state.value.password
            )
        }
        val passwordValidationResult =
            validateInputOnChange { passwordValidator(password = state.value.password) }

        val passwordErrorMessage: Int? = passwordValidationResult?.getErrorMessageResId()
        val repeatedPasswordErrorMessage: Int? =
            repeatedPasswordValidationResult?.getErrorMessageResId()

        updateState {
            this.copy(
                passwordErrorMessage = passwordErrorMessage,
                repeatedPasswordErrorMessage = repeatedPasswordErrorMessage
            )
        }
    }

    // On Submit
    private fun submitRegisterForm() {

        val formIsValid = validateForm(
            username = state.value.username,
            email = state.value.email,
            password = state.value.password,
            repeatedPassword = state.value.repeatedPassword
        )

        if (formIsValid) {
            registerUser(
                username = state.value.username,
                email = state.value.email,
                password = state.value.password,
            )
        } else {
            updateState { this.copy(formBeenSubmitted = true) }
        }
    }

    // Form Validation
    private fun validateForm(
        username: String,
        email: String,
        password: String,
        repeatedPassword: String
    ): Boolean {

        // Validate Inputs
        val usernameValidationError = usernameValidator(username).getErrorMessageResId()
        val emailValidationError = emailValidator(email).getErrorMessageResId()
        val passwordValidationError = passwordValidator(password).getErrorMessageResId()
        val repeatedPasswordValidationError =
            repeatedPasswordValidator(password, repeatedPassword).getErrorMessageResId()

        // Update states of errors
        updateState {
            copy(
                usernameErrorMessage = usernameValidationError,
                emailErrorMessage = emailValidationError,
                passwordErrorMessage = passwordValidationError,
                repeatedPasswordErrorMessage = repeatedPasswordValidationError
            )
        }

        // Check if form if valid
        val errors: List<Int?> = listOf(
            usernameValidationError,
            emailValidationError,
            passwordValidationError,
            repeatedPasswordValidationError
        )

        return errors.all { it == null }
    }

    // Data Call
    private fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            registerUseCase(username, email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        emitEffect(RegisterEffect.ShowError(result.error.toMessageRes()))
                    }

                    is Resource.Loading -> {
                        updateState { copy(isLoading = result.loading) }
                    }

                    is Resource.Success -> {
                        updateState { copy(isSuccess = true) }
                        emitEffect(RegisterEffect.NavToLogInFragment)
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
