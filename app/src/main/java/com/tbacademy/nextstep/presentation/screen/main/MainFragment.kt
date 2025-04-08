package com.tbacademy.nextstep.presentation.screen.main

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentMainBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val mainViewModel: MainViewModel by viewModels()

    override fun start() {

    }

    override fun listeners() {

        binding.btnLogOut.setOnClickListener {
            mainViewModel.logOut()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }

    override fun observers() {
    }
}