package com.example.auraFitAI.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentWelcomeCarouselBinding
import com.example.auraFitAI.presentation.auth.LoginFragment
import com.example.auraFitAI.presentation.auth.SignUpFragment
import com.example.auraFitAI.presentation.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeCarouselFragment : Fragment(R.layout.fragment_welcome_carousel) {

    private val binding by viewBinding(FragmentWelcomeCarouselBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val slides = listOf(
            OnboardingSlide(R.drawable.img_onboarding_1, "Your Fitness,\nYour Way", "Fitness", "Personalized workouts and plans\njust for you."),
            OnboardingSlide(R.drawable.img_onboarding_2, "Track Progress,\nStay Motivated", "Progress", "Monitor your stats and celebrate\nevery achievement."),
            OnboardingSlide(R.drawable.img_onboarding_3, "Better Habits,\nStronger You", "Stronger", "Build healthy habits and transform\nyour lifestyle."),
            OnboardingSlide(R.drawable.aurafit_logo, "Welcome to\nAuraFit", "AuraFit", "Start your journey to a\nhealthier, stronger you.", isFinalSlide = true)
        )
        val onboardingAdapter = OnboardingAdapter(
            slides = slides,
            onGetStartedClick = { navigateToScreen(SignUpFragment()) },
            onLogInClick = { navigateToScreen(LoginFragment()) },
            onSkipClick = { binding.viewPagerOnboarding.currentItem = slides.size - 1 }
        )

        binding.viewPagerOnboarding.adapter = onboardingAdapter
        setupIndicators(slides.size)
        setCurrentIndicator(0)

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(20, 20).apply {
            setMargins(10, 0, 10, 0)
        }
        for (i in 0 until count){
            indicators[i] = ImageView(requireContext()).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_inactive))
                this.layoutParams = layoutParams
            }
            binding.llIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.llIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.llIndicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_inactive))
            }
        }
    }

    private fun navigateToScreen(destination: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, 0, 0, 0)
            .replace(R.id.fragment_container,destination)
            .commit()
    }

}