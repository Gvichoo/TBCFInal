package com.tbacademy.nextstep.presentation.screen.main

import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentMainBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun start() {
//        val navController = findNavController()
//
//        // Ensure the starting fragment is loaded (HomeFragment) when no fragments are present
//        if (childFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null) {
//            navController.navigate(R.id.homeFragment2)
//        }
//
//        // Set up the BottomNavigationView with the NavController
//        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)


        setUpBottomNavigationBar()
    }

    override fun listeners() {}
    override fun observers() {}

    private fun setUpBottomNavigationBar(){
        val navController = childFragmentManager.findFragmentById(R.id.fragmentContainerView2)
            ?.findNavController()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController?.navigate(R.id.homeFragment2)
                    true
                }
                R.id.nav_settings -> {
                    navController?.navigate(R.id.settingsFragment)
                    true
                }
                R.id.nav_profile -> {
                    navController?.navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_Add -> {
                    navController?.navigate(R.id.addGoalFragment2)
                    true
                }
                R.id.nav_Notification -> {
                    navController?.navigate(R.id.notificationFragment)
                    true
                }
                else -> false
            }
        }
    }

//    private fun replaceFragment(fragment: Fragment) {
//        childFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView2, fragment)
//            .commit()
//    }
}