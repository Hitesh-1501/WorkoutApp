package com.example.auraFitAI.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentSignUpBinding
import com.example.auraFitAI.presentation.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val name = binding.etSignUpName.text.toString().trim()
            val email = binding.etSignUpEmail.text.toString().trim()
            val password = binding.etSignUpPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT).show()
            } else{
                // Future Hook: Store profile data directly inside Firestore users collection
            }
        }
    }
}