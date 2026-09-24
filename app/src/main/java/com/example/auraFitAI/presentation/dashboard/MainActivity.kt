package com.example.auraFitAI.presentation.dashboard

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.ActivityMainBinding
import com.example.auraFitAI.presentation.auth.AuthViewModel
import com.example.auraFitAI.presentation.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.exploreFragment, R.id.aiCoachFragment, R.id.profileFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }

        if (savedInstanceState == null) {
            if (viewModel.checkUserSession()) {
                val currentUserUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserUid != null && SessionManager.isOnboardingCompleted(this, currentUserUid)) {
                    // Start destination is homeFragment by default
                } else {
                    val bundle = Bundle().apply {
                        if (currentUserUid != null) {
                            putString("USER_UID_KEY", currentUserUid)
                        }
                    }
                    navController.navigate(R.id.onboardingFragment, bundle)
                }
            } else {
                navController.navigate(R.id.welcomeCarouselFragment)
            }
        }
    }
}
