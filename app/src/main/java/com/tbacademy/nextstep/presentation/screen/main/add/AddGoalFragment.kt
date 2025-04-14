package com.tbacademy.nextstep.presentation.screen.main.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.databinding.FragmentAddGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.add.effect.AddGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddGoalFragment : BaseFragment<FragmentAddGoalBinding>(FragmentAddGoalBinding::inflate) {

    private val addGoalViewModel: AddGoalViewModel by viewModels()

    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>


    @SuppressLint("DefaultLocale")
    override fun start() {
        initMediaPickerLauncher()
        setPickPictureBtnListener()
        setDatePicker()
    }



    override fun listeners() {
        setInputListeners()
        setSubmitBtnListener()
        setSwitchListener()
        setDeleteImageBtnListener()
    }

    override fun observers() {

        observeState()
        observeEffects()
        observeUiState()

    }

    private fun observeState(){
        collect(addGoalViewModel.state){state ->
           binding.apply {
               loaderAddGoal.loaderContainer.isVisible = state.isLoading

               tlGoalTitle.error = state.goalTitleErrorMessage?.let { getString(it) }
               tlGoalDescription.error = state.goalDescriptionErrorMessage?.let { getString(it) }
               tlTargetDate.error = state.goalDateErrorMessage?.let { getString(it) }
               tlMetricUnit.error = state.goalMetricUnitErrorMessage?.let { getString(it) }
               tlMetricTarget.error = state.goalMetricTargetErrorMessage?.let { getString(it) }


               btnCreateGoal.isEnabled = state.isCreateGoalEnabled

           }
        }
    }

    private fun observeUiState(){
        collect(addGoalViewModel.uiState){ uiState ->
            binding.apply {
                metricInputContainer.isVisible = uiState.isMetricEnabled

                uiState.imageUri?.let { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(image)
                }
            }
        }
    }

    private fun observeEffects(){
        collectLatest(addGoalViewModel.effects){effects ->
            when(effects){
                AddGoalEffect.NavToHomeFragment -> navToHomeFragment()
                is AddGoalEffect.ShowError -> showMessage(effects.message)
                AddGoalEffect.LaunchMediaPicker -> launchImagePicker()
            }
        }
    }


    private fun initMediaPickerLauncher() {
        pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                addGoalViewModel.onEvent(AddGoalEvent.ImageSelected(it))
                binding.image.setImageURI(it)
                binding.btnCancelImage.isEnabled = true
            }
        }
    }

    private fun setDeleteImageBtnListener() {
        binding.btnCancelImage.setOnClickListener {
//          binding.image.setImageResource(R.drawable.placeholder_image)
            binding.image.setImageDrawable(null)
            addGoalViewModel.onEvent(AddGoalEvent.ImageCleared)
            binding.btnCancelImage.isEnabled = false
        }
    }



    private fun setPickPictureBtnListener() {
        binding.btnSelectImage.setOnClickListener {
            addGoalViewModel.onEvent(AddGoalEvent.PickImageClicked)
        }
    }

    private fun launchImagePicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

                    calendar.set(year, month, dayOfMonth, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val date = calendar.time

                    addGoalViewModel.onEvent(AddGoalEvent.GoalDateChanged(date))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.minDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000

            datePicker.show()
        }
    }


    private fun setInputListeners(){
        setTitleInputListener()
        setDescriptionInputListener()
        setDateInputListener()
        setMetricTargetInputListener()
        setMetricUnitInputListener()
    }

    private fun setMetricTargetInputListener() {
        binding.etMetricTarget.onTextChanged { metricTarget ->
                addGoalViewModel.onEvent(AddGoalEvent.GoalMetricTargetChanged(metricTarget = metricTarget))
        }
    }


    private fun setMetricUnitInputListener(){
        binding.etMetricUnit.onTextChanged { metricUnit ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalMetricUnitChanged(metricUnit = metricUnit))
        }
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

    private fun setDateInputListener() {
        binding.etTargetDate.onTextChanged { dateString ->
            val parsedDate = parseDate(dateString)
            if (parsedDate != null) {
                addGoalViewModel.onEvent(AddGoalEvent.GoalDateChanged(date = parsedDate))
            } else {
                Log.e("AddGoalFragment", "Invalid date format: $dateString")
            }
        }
    }


    private fun parseDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    private fun setSubmitBtnListener() {
        binding.btnCreateGoal.setOnClickListener {
            addGoalViewModel.onEvent(AddGoalEvent.Submit)
        }
    }

    private fun setSwitchListener() {
        binding.switchMetricBased.setOnCheckedChangeListener { _, isChecked ->
            addGoalViewModel.onEvent(AddGoalEvent.MetricToggle(isChecked))
        }
    }


    private fun navToHomeFragment(){
        findNavController().navigate(
            AddGoalFragmentDirections.actionNavAddToNavHome()
        )
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}