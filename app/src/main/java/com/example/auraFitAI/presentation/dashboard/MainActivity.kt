package com.example.auraFitAI.presentation.dashboard


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.ActivityMainBinding
import com.example.auraFitAI.presentation.auth.AuthViewModel
import com.example.auraFitAI.presentation.onboarding.WelcomeCarouselFragment
import com.example.auraFitAI.presentation.home.HomeFragment
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

        if (savedInstanceState == null) {
            if(viewModel.checkUserSession()){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }else{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, WelcomeCarouselFragment())
                    .commit()
            }
        }
    }
}