package com.example.auraFitAI.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentOnboardingBinding
import com.example.auraFitAI.presentation.util.viewBinding

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {
    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropDownMenu()
        val age = binding.etAge.text.toString()
        val height = binding.etHeight.text.toString()
        val weight = binding.etWeight.text.toString()
        val goal = binding.actvFitnessGoal.text.toString()

        if (age.isEmpty() || height.isEmpty() || weight.isEmpty() || goal.isEmpty()) {
            Toast.makeText(requireContext(), "Please populate all biometric fields", Toast.LENGTH_SHORT).show()
        } else {
            // Future Hook: Store profile data directly inside Firestore users collection
        }
    }

    private fun setupDropDownMenu(){
        val fitnessGoal = arrayOf("Lose Weight", "Build Muscle", "Stay Fit", "Increase Endurance")
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,fitnessGoal)
        binding.actvFitnessGoal.setAdapter(adapter)
    }
}