package com.tbacademy.nextstep.presentation.screen.authentication.login

import android.content.ContentValues.TAG
import android.credentials.CredentialManager
import android.util.Log
import androidx.core.view.isVisible
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentLoginBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.authentication.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.authentication.login.event.LoginEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {




    private val loginViewModel: LoginViewModel by viewModels()


    override fun start() {

    }

    override fun listeners() {
        setLogInBtnListener()
        setRegisterBtnListener()
        setRememberMeCheckboxListener()
        setInputListeners()
    }

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun observeState() {
        collect(flow = loginViewModel.state) { state ->
            binding.apply {
                loaderLogin.loaderContainer.isVisible = state.isLoading

                tlEmail.error = state.emailErrorMessage?.let { getString(it) }
                tlPassword.error = state.passwordErrorMessage?.let { getString(it) }

                btnLogin.isEnabled = state.isLogInEnabled
            }
        }
    }

    private fun setLogInBtnListener() {
        binding.btnLogin.setOnClickListener {
            loginViewModel.onEvent(LoginEvent.Submit)
        }
    }

    private fun setRegisterBtnListener() {
        binding.btnRegister.setOnClickListener {
            loginViewModel.onEvent(LoginEvent.RegisterButtonClicked)
        }
    }


    private fun setRememberMeCheckboxListener() {
        binding.cbRememberMe.isChecked = loginViewModel.uiState.value.rememberMe

        binding.cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
            loginViewModel.onEvent(LoginEvent.RememberMeChanged(isChecked))

        }
    }

    private fun setInputListeners() {
        setEmailInputListener()
        setPasswordInputListener()
    }

    private fun setEmailInputListener() {
        binding.etEmail.onTextChanged { email ->
            loginViewModel.onEvent(LoginEvent.EmailChanged(email = email))
        }
    }

    private fun setPasswordInputListener() {
        binding.etPassword.onTextChanged { password ->
            loginViewModel.onEvent(LoginEvent.PasswordChanged(password = password))
        }
    }

    private fun observeEffect() {
        collectLatest(loginViewModel.effects) {
            when (it) {
                LoginEffect.NavToMainFragment -> navToMainFragment()
                LoginEffect.NavToRegisterFragment -> navToRegisterFragment()
                is LoginEffect.ShowError -> showMessage(it.message)
            }
        }
    }

    private fun navToRegisterFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navToMainFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }


    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }


}