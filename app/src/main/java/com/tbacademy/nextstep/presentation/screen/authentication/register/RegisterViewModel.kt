package com.tbacademy.nextstep.presentation.screen.authentication.register

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.InputValidationResult
import com.tbacademy.nextstep.domain.core.getErrorMessageResId
import com.tbacademy.nextstep.domain.usecase.register.RegisterUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateUsernameUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidatePasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateRepeatedPasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateEmailUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.authentication.register.effect.RegisterEffect
import com.tbacademy.nextstep.presentation.screen.authentication.register.event.RegisterEvent
import com.tbacademy.nextstep.presentation.screen.authentication.register.state.RegisterState
import com.tbacademy.nextstep.presentation.screen.authentication.register.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCase,
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel<RegisterState, RegisterEvent, RegisterEffect>(RegisterState()) {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> get() = _uiState


    override fun onEvent(event: RegisterEvent) {
        when (event) {

            is RegisterEvent.UsernameChanged -> onUsernameChanged(username = event.username)
            is RegisterEvent.EmailChanged -> onEmailChanged(email = event.email)
            is RegisterEvent.PasswordChanged -> onPasswordChanged(password = event.password)
            is RegisterEvent.RepeatedPasswordChanged -> onRepeatedPasswordChanged(repeatedPassword = event.repeatedPassword)

            is RegisterEvent.PasswordVisibilityToggle -> onPasswordVisibilityToggle()
            is RegisterEvent.RepeatedPasswordVisibilityToggle -> onRepeatedPasswordVisibilityToggle()

            is RegisterEvent.Submit -> submitRegisterForm()

            is RegisterEvent.OnSignUpBtnClicked -> validateForm(
                event.nickname,
                event.email,
                event.password,
                event.repeatedPassword
            )

            is RegisterEvent.OnLogInBtnClicked -> viewModelScope.launch { emitEffect(RegisterEffect.NavToLogInFragment) }
        }
    }

    private fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username) }

        val usernameValidationResult =
            validateInputOnChange { validateUsernameUseCase(username = username) }
        val usernameErrorMessage: Int? = usernameValidationResult?.getErrorMessageResId()
        updateState { this.copy(usernameErrorMessage = usernameErrorMessage) }
    }

    private fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }

        val emailValidationResult = validateInputOnChange { validateEmailUseCase(email = email) }
        val emailErrorMessage: Int? = emailValidationResult?.getErrorMessageResId()
        updateState { this.copy(emailErrorMessage = emailErrorMessage) }
    }

    private fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }

        val passwordValidationResult =
            validateInputOnChange { validatePasswordUseCase(password = password) }

        val repeatedPasswordValidationResult = validateInputOnChange {
            validateRepeatedPasswordUseCase(
                repeatedPassword = uiState.value.repeatedPassword,
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

    private fun onRepeatedPasswordChanged(repeatedPassword: String) {
        _uiState.update { it.copy(repeatedPassword = repeatedPassword) }

        val repeatedPasswordValidationResult = validateInputOnChange {
            validateRepeatedPasswordUseCase(
                repeatedPassword = repeatedPassword,
                password = uiState.value.password
            )
        }

        val passwordValidationResult =
            validateInputOnChange { validatePasswordUseCase(password = uiState.value.password) }

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

    private fun onPasswordVisibilityToggle() {
        _uiState.update { it.copy(isPasswordVisible = !uiState.value.isPasswordVisible) }
    }

    private fun onRepeatedPasswordVisibilityToggle() {
        _uiState.update { it.copy(isRepeatedPasswordVisible = !uiState.value.isRepeatedPasswordVisible) }
    }

    private fun submitRegisterForm() {

        val formIsValid = validateForm(
            username = uiState.value.username,
            email = uiState.value.email,
            password = uiState.value.password,
            repeatedPassword = uiState.value.repeatedPassword
        )


        if (formIsValid) {
            registerUser(
                username = uiState.value.username,
                email = uiState.value.email,
                password = uiState.value.password,
            )
        } else {
            updateState { this.copy(isFormSubmitted = true) }
        }
    }


    private fun validateForm(
        username: String,
        email: String,
        password: String,
        repeatedPassword: String
    ): Boolean {

        // Validate Inputs
        val usernameValidationError = validateUsernameUseCase(username).getErrorMessageResId()
        val emailValidationError = validateEmailUseCase(email).getErrorMessageResId()
        val passwordValidationError = validatePasswordUseCase(password).getErrorMessageResId()
        val repeatedPasswordValidationError =
            validateRepeatedPasswordUseCase(password, repeatedPassword).getErrorMessageResId()

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

    private fun registerUser(username: String, email: String, password: String) {
        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            registerUseCase(username, email, password).collect { result ->
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

    // Helpers
    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.isFormSubmitted)
            validator()
        else null
    }
}
