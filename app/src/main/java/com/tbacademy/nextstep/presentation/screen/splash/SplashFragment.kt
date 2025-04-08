package com.tbacademy.nextstep.presentation.screen.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentSplashBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//Bla Bla Splash
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun start() {
        checkSessionAndNavigate()
    }

    override fun listeners() {

    }

    override fun observers() {

    }


    private fun checkSessionAndNavigate() {
        lifecycleScope.launch {

            val isRememberMeEnabled = splashViewModel.isRememberMeEnabled()
            if (isRememberMeEnabled) {
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

}