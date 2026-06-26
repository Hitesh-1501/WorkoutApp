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
import com.example.auraFitAI.databinding.FragmentLoginBinding
import com.example.auraFitAI.domain.util.UiState
import com.example.auraFitAI.presentation.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModelState()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all layout fields", Toast.LENGTH_SHORT).show()
            } else {
              viewModel.login(email,password)
            }
        }

        binding.tvNavigateToSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in,0,0,0)
                .replace(R.id.fragment_container, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModelState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loginState.collect { uiState ->
                    when(uiState){
                        is UiState.Loading -> {
                            toggleLoadingViews(isLoading = true)
                        }
                        is UiState.Success -> {
                            toggleLoadingViews(isLoading = false)
                            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Error -> {
                            toggleLoadingViews(isLoading = false)
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Empty -> {
                            toggleLoadingViews(isLoading = false)
                        }
                    }
                }
            }
        }
    }


    private fun toggleLoadingViews(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
    }
}