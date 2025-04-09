package com.tbacademy.nextstep.presentation.screen.splash

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentSplashBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//Bla Bla Splash
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun start() {}
    override fun listeners() {}

    override fun observers() {
        observeEffect()
    }

    private fun observeEffect() {
        collect(flow = splashViewModel.effect) { effect ->
            when (effect) {
                is SplashEffect.NavigateToLogin -> {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    )
                }

                is SplashEffect.NavigateToMain -> {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToMainFragment()
                    )
                }
            }
        }
    }
}