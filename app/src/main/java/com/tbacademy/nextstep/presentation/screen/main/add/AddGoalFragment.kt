package com.tbacademy.nextstep.presentation.screen.main.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.fragment.app.viewModels
import com.tbacademy.nextstep.databinding.FragmentAddGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.getString
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
    }

    override fun observers() {

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
                Log.d("CREATE_GOAL", "BTN_CLICKED")
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

}