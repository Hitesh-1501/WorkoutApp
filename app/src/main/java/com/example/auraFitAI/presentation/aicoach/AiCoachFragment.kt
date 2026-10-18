package com.example.auraFitAI.presentation.aicoach

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentAiCoachBinding
import com.example.auraFitAI.presentation.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiCoachFragment : Fragment(R.layout.fragment_ai_coach) {
    private val binding by viewBinding(FragmentAiCoachBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardPrompt1.setOnClickListener {
            showAiAdviceDialog(
                "Core Workout Routine (10 Mins)",
                "1. Plank (60s)\n2. Bicycle Crunches (45s)\n3. Russian Twists (45s)\n4. Mountain Climbers (45s)\n5. Leg Raises (45s)\n\nRepeat 2 circuits with 20 seconds rest between exercises for maximum core activation and endurance!"
            )
        }

        binding.cardPrompt2.setOnClickListener {
            showAiAdviceDialog(
                "Pushup Form Guide",
                "1. Hand Placement: Place hands slightly wider than shoulder-width apart.\n2. Body Alignment: Keep your body in a straight line from head to heels (engage core and glutes).\n3. Elbow Angle: Tuck elbows at a 45-degree angle relative to your torso.\n4. Full Range: Lower your chest until it's just above the floor, then push back up explosively."
            )
        }
    }

    private fun showAiAdviceDialog(title: String, advice: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(advice)
            .setPositiveButton("Got It!") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
