package com.tbacademy.nextstep.presentation.screen.main.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.databinding.FragmentAddGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.getString
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.add.effect.AddGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AddGoalFragment : BaseFragment<FragmentAddGoalBinding>(FragmentAddGoalBinding::inflate) {

    private val addGoalViewModel: AddGoalViewModel by viewModels()

    @SuppressLint("DefaultLocale")
    override fun start() {
        setDatePicker()
    }

    override fun listeners() {
        setCreateGoalBtnListener()
        setInputListeners()
        setSignInBtnListener()
    }

    override fun observers() {

        observeState()
        observeEffects()

    }




    private fun observeState(){
        collect(addGoalViewModel.state){state ->
           binding.apply {
               loaderAddGoal.loaderContainer.isVisible = state.isLoading

               tlGoalTitle.error = state.goalTitleErrorMessage?.let { getString(it) }
               tlGoalDescription.error = state.goalDescriptionErrorMessage?.let { getString(it) }


               btnCreateGoal.isEnabled = state.isCreateGoalEnabled
           }
        }
    }

    private fun observeEffects(){
        collectLatest(addGoalViewModel.effects){effects ->
            when(effects){
                AddGoalEffect.NavToHomeFragment -> navToHomeFragment()
                is AddGoalEffect.ShowError -> showMessage(effects.message)
            }
        }
    }




    private fun setCreateGoalBtnListener() {
        binding.apply {
            btnCreateGoal.setOnClickListener {
                addGoalViewModel.onEvent(
                    AddGoalEvent.CreateGoal(
                        title = etGoalTitle.getString(),
                        description = etGoalDescription.getString(),
                        goalDate = Date()
                    )
                )
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setDatePicker() {
        binding.etTargetDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.etTargetDate.setText(formattedDate)

                    // Set selected date
                    calendar.set(year, month, dayOfMonth, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val date = calendar.time
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // ✅ Disallow today and past
            datePicker.datePicker.minDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000

            datePicker.show()
        }
    }

    private fun setInputListeners(){
        setTitleInputListener()
        setDescriptionInputListener()
    }

    private fun setTitleInputListener() {
        binding.etGoalTitle.onTextChanged { title ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalTitleChanged(title = title))
        }
    }

    private fun setDescriptionInputListener() {
        binding.etGoalDescription.onTextChanged { description ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalDescriptionChanged(description = description))
        }
    }

    private fun setSignInBtnListener() {
        binding.btnCreateGoal.setOnClickListener {
            addGoalViewModel.onEvent(AddGoalEvent.Submit)
        }
    }


    private fun navToHomeFragment(){
//        findNavController().navigate(
//            AddGoalFragmentDirections.actionAddGoalFragmentToHomeFragment()
//        )
    }




    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }

}