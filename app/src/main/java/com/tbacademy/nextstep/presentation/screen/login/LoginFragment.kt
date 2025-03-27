package com.tbacademy.nextstep.presentation.screen.login

import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentLoginBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.login.effect.LoginEffect
import com.tbacademy.nextstep.presentation.screen.login.event.LoginEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun start() {

        logInButtonClicked()

        registerButtonClicked()

        observeState()

        observeEffect()

        receiveEmailAndPasswordFromRegister()

    }

    private fun logInButtonClicked(){
        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            loginViewModel.obtainEvent(LoginEvent.LoginButtonClicked(email, password))
        }
    }

    private fun registerButtonClicked() {
        binding.btnRegister.setOnClickListener {
            loginViewModel.obtainEvent(LoginEvent.RegisterButtonClicked)
        }
    }

    private fun observeState(){
        collect(loginViewModel.viewState){
            binding.loader.visibility = if (it.isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun observeEffect(){
        collectLatest(loginViewModel.effects){
            when(it){
                LoginEffect.NavToMainFragment -> navToMainFragment()
                LoginEffect.NavToRegisterFragment -> navToRegisterFragment()
                is LoginEffect.ShowError -> showMessage(it.message)
            }
        }
    }

    private fun receiveEmailAndPasswordFromRegister(){
        setFragmentResultListener("registration_request_key") { _, bundle ->
            val email = bundle.getString("email")
            val password = bundle.getString("password")
            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }
    }


    private fun navToRegisterFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun navToMainFragment(){
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }


    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}