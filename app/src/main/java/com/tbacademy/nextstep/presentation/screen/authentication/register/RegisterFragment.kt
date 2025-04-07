package com.tbacademy.nextstep.presentation.screen.authentication.register

import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentRegisterBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.extension.togglePasswordVisibility
import com.tbacademy.nextstep.presentation.screen.authentication.register.effect.RegisterEffect
import com.tbacademy.nextstep.presentation.screen.authentication.register.event.RegisterEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun start() {

    }

    override fun listeners() {
        setRegisterButtonClickListener()
        startSignInClickListener()
        setInputListeners()
        setPasswordVisibilityIconListener()
        setRepeatedPasswordVisibilityIconListener()
    }

    override fun observers() {
        observeState()
        observeUiState()
        observeEffect()
    }

    private fun observeState() {
        collect(registerViewModel.state) { state ->
            binding.apply {
                loader.loaderContainer.isVisible = state.isLoading

                tlUserName.error = state.usernameErrorMessage?.let { getString(it) }
                tlEmail.error = state.emailErrorMessage?.let { getString(it) }

                tlPassword.error = state.passwordErrorMessage?.let { getString(it) }

                tlRepeatPassword.error = state.repeatedPasswordErrorMessage?.let { getString(it) }

                btnRegister.isEnabled = state.isSignUpEnabled
            }
        }
    }

    private fun observeUiState() {
        collect(flow = registerViewModel.uiState) { state ->
            binding.apply {
                etPassword.togglePasswordVisibility(state.isPasswordVisible)
                etRepeatPassword.togglePasswordVisibility(state.isRepeatedPasswordVisible)
            }
        }
    }

    private fun observeEffect() {
        collectLatest(registerViewModel.effects) { effects ->
            when (effects) {
                is RegisterEffect.NavToLogInFragment -> navToLoginFragment()
                is RegisterEffect.ShowError -> showMessage(effects.message)
            }
        }
    }

    private fun setInputListeners() {
        setUsernameInputListener()
        setEmailInputListener()
        setPasswordInputListener()
        setRepeatPasswordInputListener()
    }

    private fun setRegisterButtonClickListener() {
        binding.btnRegister.setOnClickListener {
            registerViewModel.onEvent(RegisterEvent.Submit)
        }
    }

    private fun startSignInClickListener() {
        binding.btnLogin.setOnClickListener {
            registerViewModel.onEvent(RegisterEvent.OnLogInBtnClicked)
        }
    }

    private fun setUsernameInputListener() {
        binding.etUsername.onTextChanged { username ->
            registerViewModel.onEvent(RegisterEvent.UsernameChanged(username = username))
        }
    }

    private fun setEmailInputListener() {
        binding.etEmail.onTextChanged { email ->
            registerViewModel.onEvent(RegisterEvent.EmailChanged(email = email))
        }
    }

    private fun setPasswordInputListener() {
        binding.etPassword.onTextChanged { password ->
            registerViewModel.onEvent(RegisterEvent.PasswordChanged(password = password))
        }
    }

    private fun setRepeatPasswordInputListener() {
        binding.etRepeatPassword.onTextChanged { repeatedPassword ->
            registerViewModel.onEvent(RegisterEvent.RepeatedPasswordChanged(repeatedPassword = repeatedPassword))
        }
    }

    private fun setPasswordVisibilityIconListener() {
        binding.tlPassword.setEndIconOnClickListener {
            registerViewModel.onEvent(RegisterEvent.PasswordVisibilityToggle)
        }
    }

    private fun setRepeatedPasswordVisibilityIconListener() {
        binding.tlRepeatPassword.setEndIconOnClickListener {
            registerViewModel.onEvent(RegisterEvent.RepeatedPasswordVisibilityToggle)
        }
    }

    private fun navToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun showMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}