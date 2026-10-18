package com.example.auraFitAI.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentProfileBinding
import com.example.auraFitAI.presentation.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser?.email != null) {
            binding.tvUserEmail.text = currentUser.email
        }

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
}
