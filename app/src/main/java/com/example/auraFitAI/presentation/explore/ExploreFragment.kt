package com.example.auraFitAI.presentation.explore

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentExploreBinding
import com.example.auraFitAI.presentation.util.viewBinding
import com.example.auraFitAI.presentation.workout.ExerciseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {
    private val binding by viewBinding(FragmentExploreBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardCategoryFullBody.setOnClickListener {
            startActivity(Intent(requireContext(), ExerciseActivity::class.java))
        }
        binding.cardCategoryCore.setOnClickListener {
            startActivity(Intent(requireContext(), ExerciseActivity::class.java))
        }
        binding.cardCategoryCardio.setOnClickListener {
            startActivity(Intent(requireContext(), ExerciseActivity::class.java))
        }
        binding.cardCategoryUpper.setOnClickListener {
            startActivity(Intent(requireContext(), ExerciseActivity::class.java))
        }
        binding.cardCategoryLower.setOnClickListener {
            startActivity(Intent(requireContext(), ExerciseActivity::class.java))
        }
    }
}
