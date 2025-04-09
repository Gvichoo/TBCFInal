package com.tbacademy.nextstep.presentation.screen.main.settings

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.databinding.FragmentSettingsBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.screen.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val mainViewModel: SettingsViewModel by viewModels()

    override fun start() {

    }

    override fun listeners() {

        binding.btnLogOut.setOnClickListener {
            mainViewModel.logOut()
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToLoginFragment()
            )
        }
    }

    override fun observers() {
    }
}