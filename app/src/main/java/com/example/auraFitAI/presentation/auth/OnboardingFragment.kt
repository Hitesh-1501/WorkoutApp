package com.example.auraFitAI.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentOnboardingBinding
import com.example.auraFitAI.domain.util.UiState
import com.example.auraFitAI.presentation.home.HomeFragment
import com.example.auraFitAI.presentation.util.viewBinding
import kotlinx.coroutines.launch

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {
    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val viewModel: AuthViewModel by viewModels()
    private var userUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userUid = it.getString("USER_UID_KEY")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropDownMenu()
        setupListeners()
        observeViewModelState()
    }

    private fun setupDropDownMenu(){
        val fitnessGoal = arrayOf("Lose Weight", "Build Muscle", "Stay Fit", "Increase Endurance")
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,fitnessGoal)
        binding.actvFitnessGoal.setAdapter(adapter)
    }

    private fun setupListeners(){
        binding.btnCompleteProfile.setOnClickListener {
            val ageStr = binding.etAge.text.toString().trim()
            val heightStr = binding.etHeight.text.toString().trim()
            val weightStr = binding.etWeight.text.toString().trim()
            val goal = binding.actvFitnessGoal.text.toString().trim()

            if (ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || goal.isEmpty()) {
                Toast.makeText(requireContext(), "Please populate all biometric fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = userUid
            if (uid == null) {
                Toast.makeText(requireContext(), "User session token missing. Please register again.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val age = ageStr.toInt()
            val height = heightStr.toDouble()
            val weight = weightStr.toDouble()

            viewModel.submitOnboarding(uid, age, height, weight, goal)
        }
    }

    private fun observeViewModelState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.onboardingState.collect { uiState ->
                    when(uiState) {
                        is UiState.Loading -> {
                            binding.btnCompleteProfile.isEnabled = false
                            binding.btnCompleteProfile.text = "Synchronizing Profile..."
                        }
                        is UiState.Success -> {
                            binding.btnCompleteProfile.isEnabled = true
                            Toast.makeText(requireContext(), "Setup Complete! Welcome aboard.", Toast.LENGTH_SHORT).show()

                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, 0, 0, 0)
                                .replace(R.id.fragment_container, HomeFragment())
                                .commit()
                        }

                        is UiState.Error -> {
                            binding.btnCompleteProfile.isEnabled = true
                            binding.btnCompleteProfile.text = "Complete Setup"
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
                        }
                        is UiState.Empty -> {
                            binding.btnCompleteProfile.isEnabled = true
                            binding.btnCompleteProfile.text = "Complete Setup"
                        }
                    }
                }
            }
        }
    }
}