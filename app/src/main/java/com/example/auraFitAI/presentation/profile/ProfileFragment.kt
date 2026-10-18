package com.example.auraFitAI.presentation.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentProfileBinding
import com.example.auraFitAI.presentation.util.viewBinding
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

        binding.cardOptionTheme.setOnClickListener {
            val themes = arrayOf("Light Mode", "Dark Mode", "System Default")
            AlertDialog.Builder(requireContext())
                .setTitle("Choose Theme Mode")
                .setItems(themes) { _, which ->
                    when (which) {
                        0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
                .show()
        }

        binding.cardOptionLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}
