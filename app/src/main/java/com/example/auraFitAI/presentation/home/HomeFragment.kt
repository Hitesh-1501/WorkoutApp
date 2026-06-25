package com.example.auraFitAI.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentHomeBinding
import com.example.auraFitAI.presentation.profile.BMIActivity
import com.example.auraFitAI.presentation.profile.HistoryActivity
import com.example.auraFitAI.presentation.util.viewBinding
import com.example.auraFitAI.presentation.workout.ExerciseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private var directDownloadLink : String = "https://drive.google.com/file/d/1jpRUFgtFIC9i1rL4y6a6c8V2pRh65B2q/view?usp=sharing"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        runEntranceAnimation()
    }

    private fun setupClickListeners() {
        // Core Intent Navigation Loops - Updated to utilize fragment context
        binding.flStart.setOnClickListener {
            val intent = Intent(requireContext(), ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.flBMI.setOnClickListener {
            val intent = Intent(requireContext(), BMIActivity::class.java)
            startActivity(intent)
        }

        binding.flHistory.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.flShare.setOnClickListener {
            shareAPK()
        }
    }

    private fun runEntranceAnimation() {
        // Initially hide interactive view models
        setViewsVisibility(View.INVISIBLE)

        // Load your structural fade_in XML asset configurations
        // Note: Make sure R.anim.fade_in exists inside your res/anim directory
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        fadeInAnimation.duration = 2000

        // Start animation loop on dashboard header card
        binding.myImageView.startAnimation(fadeInAnimation)

        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Smoothly toggle metrics visible once background image process completes
                setViewsVisibility(View.VISIBLE)

                // Animate secondary button arrivals concurrently
                val secondaryFade = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
                binding.flStart.startAnimation(secondaryFade)
                binding.flBMI.startAnimation(secondaryFade)
                binding.flHistory.startAnimation(secondaryFade)
                binding.flShare.startAnimation(secondaryFade)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun setViewsVisibility(visibility: Int) {
        binding.flStart.visibility = visibility
        binding.flBMI.visibility = visibility
        binding.flHistory.visibility = visibility
        binding.flShare.visibility = visibility
        binding.tvHistory.visibility = visibility
        binding.tvCalculator.visibility = visibility
        binding.tvShare.visibility = visibility
    }

    private fun shareAPK() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
            val shareMessage = "AuraFit AI app, Download the app Now: $directDownloadLink\n\n"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

}