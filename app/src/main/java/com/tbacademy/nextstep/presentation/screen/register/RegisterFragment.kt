package com.tbacademy.nextstep.presentation.screen.register

import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentRegisterBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.register.effect.RegisterEffect
import com.tbacademy.nextstep.presentation.screen.register.event.RegisterEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun start() {
    }

    override fun listeners() {
        setRegisterButtonClickListener()
        startSignInClickListener()
    }

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun observeState() {
        collect(registerViewModel.viewState) { state ->
            binding.loader.isVisible = state.isLoading
        }
    }

    private fun setRegisterButtonClickListener() {
        binding.btnRegister.setOnClickListener {
            val nickname = binding.etUserName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val passwordRepeated = binding.etRepeatPassword.text.toString().trim()

            registerViewModel.onEvent(
                RegisterEvent.SignUpButtonClicked(
                    nickname,
                    email,
                    password,
                    passwordRepeated
                )
            )
        }
    }

    private fun observeEffect() {
        collectLatest(registerViewModel.effects) { effects ->
            when (effects) {
                RegisterEffect.NavToLogInFragment -> navToLoginFragment()
                is RegisterEffect.ShowError -> showMessage(effects.message)
            }
        }
    }

    private fun startSignInClickListener() {
        binding.btnLogin.setOnClickListener {
            registerViewModel.onEvent(RegisterEvent.LogInButtonClicked)
        }
    }

    private fun navToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }


    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}