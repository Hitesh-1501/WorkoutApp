package com.example.auraFitAI.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentProfileBinding
import com.example.auraFitAI.presentation.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.cardOptionBmi.setOnClickListener {
            startActivity(Intent(requireContext(), BMIActivity::class.java))
        }

        binding.cardOptionHistory.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        binding.cardOptionSettings.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("App Settings & Preferences")
                .setMessage("• App Version: 1.0 (AuraFit AI)\n• Text-to-Speech Voice Prompts: Enabled\n• Notification Reminders: Active\n• Database: Room & Firebase Firestore Sync")
                .setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.cardOptionLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.email.isNotEmpty()) {
                        binding.tvUserEmail.text = state.email
                    }

                    binding.tvStreakValue.text = "${state.streakDays} Days"
                    binding.tvWorkoutsValue.text = "${state.totalWorkouts} Sessions"

                    binding.tvMetricGoal.text = "🎯 Goal: ${state.fitnessGoal}"
                    binding.tvMetricWeightHeight.text = "⚖️ Weight: ${state.weight} kg | Height: ${state.height} cm (Age: ${state.age})"
                    binding.tvMetricActivityLevel.text = "🏃 Activity Level: ${state.activityLevel} (${state.gender})"
                }
            }
        }
    }
}
