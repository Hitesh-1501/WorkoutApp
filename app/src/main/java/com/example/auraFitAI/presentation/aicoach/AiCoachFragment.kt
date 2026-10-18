package com.example.auraFitAI.presentation.aicoach

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentAiCoachBinding
import com.example.auraFitAI.presentation.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiCoachFragment : Fragment(R.layout.fragment_ai_coach) {
    private val binding by viewBinding(FragmentAiCoachBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardPrompt1.setOnClickListener {
            showAiResponse(
                "AuraFit Core Workout Advice",
                "For a 10-minute core blitz, perform 45 seconds of work followed by 15 seconds of rest: 1. Plank Hold 2. Bicycle Crunches 3. Leg Raises 4. Russian Twists. Repeat for 2 full rounds!"
            )
        }

        binding.cardPrompt2.setOnClickListener {
            showAiResponse(
                "AuraFit Form Guidance",
                "To perfect your pushup form: Keep your core tight, stack your wrists directly under your shoulders, lower your chest until it's an inch from the floor, and keep your elbows tucked at a 45-degree angle."
            )
        }
    }

    private fun showAiResponse(title: String, advice: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(advice)
            .setPositiveButton("Got It!") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
