package com.tbacademy.nextstep.presentation.screen.main

import androidx.fragment.app.Fragment
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentMainBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.screen.main.add.AddGoalFragment
import com.tbacademy.nextstep.presentation.screen.main.home.HomeFragment
import com.tbacademy.nextstep.presentation.screen.main.notification.NotificationFragment
import com.tbacademy.nextstep.presentation.screen.main.profile.ProfileFragment
import com.tbacademy.nextstep.presentation.screen.main.settings.SettingsFragment

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun start() {

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.selectedItemId = R.id.nav_home

        setUpBottomNavigationBar()

    }

    override fun listeners() {}
    override fun observers() {}

    private fun setUpBottomNavigationBar(){
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.nav_Add -> {
                    replaceFragment(AddGoalFragment())
                    true
                }
                R.id.nav_Notification -> {
                    replaceFragment(NotificationFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}