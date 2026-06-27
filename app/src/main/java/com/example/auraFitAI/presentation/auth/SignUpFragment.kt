package com.example.auraFitAI.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentSignUpBinding
import com.example.auraFitAI.domain.util.UiState
import com.example.auraFitAI.presentation.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModelState()
    }

    private fun setupListeners() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.etSignUpName.text.toString().trim()
            val email = binding.etSignUpEmail.text.toString().trim()
            val password = binding.etSignUpPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(email, password)
            }
        }

        binding.tvNavigateToLogin.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signUpState.collect { uiState ->
                    when(uiState){
                        is UiState.Loading -> {
                            toggleLoadingViews(isLoading = true)
                        }
                        is UiState.Success -> {
                            toggleLoadingViews(isLoading = false)
                            val userUid = uiState.data
                            val onboardingFragment = OnboardingFragment().apply {
                                arguments = Bundle().apply {
                                    putString("USER_UID_KEY", userUid)
                                }
                            }

                            parentFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,0,0,0)
                                .replace(R.id.fragment_container, onboardingFragment)
                                .commit()
                        }
                        is UiState.Error -> {
                            toggleLoadingViews(isLoading = false)
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG).show()
                        }
                        is UiState.Empty -> {
                            toggleLoadingViews(isLoading = false)
                        }
                    }
                }
            }
        }
    }


    private fun toggleLoadingViews(isLoading: Boolean){
        binding.pbSignUpLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSignUp.isEnabled = !isLoading
    }
}