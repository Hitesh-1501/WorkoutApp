package com.example.auraFitAI.presentation.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
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

        binding.composeBottomBar.setContent {
            var currentDestId by remember { mutableStateOf(navController.currentDestination?.id ?: R.id.homeFragment) }

            DisposableEffect(navController) {
                val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                    currentDestId = destination.id
                }
                navController.addOnDestinationChangedListener(listener)
                onDispose {
                    navController.removeOnDestinationChangedListener(listener)
                }
            }

            val showBar = when (currentDestId) {
                R.id.homeFragment, R.id.exploreFragment, R.id.aiCoachFragment, R.id.profileFragment -> true
                else -> false
            }

            if (showBar) {
                FloatingCurvedBottomBar(
                    currentDestinationId = currentDestId,
                    onItemSelected = { destination ->
                        if (navController.currentDestination?.id != destination.routeId) {
                            navController.navigate(destination.routeId) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
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
