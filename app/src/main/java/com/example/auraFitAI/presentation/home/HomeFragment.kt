package com.example.auraFitAI.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentHomeBinding
import com.example.auraFitAI.presentation.profile.BMIActivity
import com.example.auraFitAI.presentation.profile.HistoryActivity
import com.example.auraFitAI.presentation.util.viewBinding
import com.example.auraFitAI.presentation.workout.ExerciseActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val directDownloadLink: String = "https://drive.google.com/file/d/1jpRUFgtFIC9i1rL4y6a6c8V2pRh65B2q/view?usp=sharing"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val workoutItems = listOf(
            HomeWorkoutItem(
                id = 1,
                title = "Full Body HIIT",
                subtitle = "High intensity fat burn circuit",
                duration = "7 Mins",
                cardType = WorkoutCardType.WORKOUT,
                iconRes = R.drawable.ic_aurafit_logo,
                gradientColors = listOf(0xFF1B5E20, 0xFF00C99E)
            ),
            HomeWorkoutItem(
                id = 2,
                title = "Core & Abs Strength",
                subtitle = "Strengthen midsection & stability",
                duration = "7 Mins",
                cardType = WorkoutCardType.WORKOUT,
                iconRes = R.drawable.ic_goal,
                gradientColors = listOf(0xFF02B290, 0xFF00A86B)
            ),
            HomeWorkoutItem(
                id = 3,
                title = "BMI Calculator",
                subtitle = "Check your body mass index",
                duration = "Tool",
                cardType = WorkoutCardType.BMI,
                iconRes = R.drawable.ic_height,
                gradientColors = listOf(0xFF222531, 0xFF161820)
            ),
            HomeWorkoutItem(
                id = 4,
                title = "Workout History",
                subtitle = "View completed fitness sessions",
                duration = "Logs",
                cardType = WorkoutCardType.HISTORY,
                iconRes = R.drawable.baseline_history_24,
                gradientColors = listOf(0xFF222531, 0xFF161820)
            ),
            HomeWorkoutItem(
                id = 5,
                title = "Share AuraFit AI",
                subtitle = "Invite friends to workout together",
                duration = "Social",
                cardType = WorkoutCardType.SHARE,
                iconRes = R.drawable.baseline_share_24,
                gradientColors = listOf(0xFF222531, 0xFF161820)
            )
        )

        val adapter = HomeWorkoutAdapter(workoutItems) { item ->
            when (item.cardType) {
                WorkoutCardType.WORKOUT -> {
                    val intent = Intent(requireContext(), ExerciseActivity::class.java)
                    startActivity(intent)
                }
                WorkoutCardType.BMI -> {
                    val intent = Intent(requireContext(), BMIActivity::class.java)
                    startActivity(intent)
                }
                WorkoutCardType.HISTORY -> {
                    val intent = Intent(requireContext(), HistoryActivity::class.java)
                    startActivity(intent)
                }
                WorkoutCardType.SHARE -> {
                    shareAPK()
                }
            }
        }

        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWorkouts.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.flStart.setOnClickListener {
            val intent = Intent(requireContext(), ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.cardHero.setOnClickListener {
            val intent = Intent(requireContext(), ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.loginFragment)
        }
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
