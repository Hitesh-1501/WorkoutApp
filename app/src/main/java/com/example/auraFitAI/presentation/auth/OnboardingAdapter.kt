package com.example.auraFitAI.presentation.auth

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.ItemOnboardingPageBinding

class OnboardingAdapter(
    private val slides: List<OnboardingSlide>,
    private val onGetStartedClick: () -> Unit,
    private val onLogInClick: () -> Unit,
    private val onSkipClick: () -> Unit
): RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingViewHolder {
        val binding = ItemOnboardingPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int
    ) {
        holder.bind(slides[position])
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    inner class  OnboardingViewHolder(private val binding: ItemOnboardingPageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(slide: OnboardingSlide){
            binding.ivIllustration.setImageResource(slide.imageRes)
            binding.tvSlideDescription.text = slide.description

            val titleText = slide.title
            val spannableString = SpannableString(titleText)
            val startIdx = titleText.indexOf(slide.highlightedWord)

            if (startIdx != -1){
                val endIdx = startIdx + slide.highlightedWord.length
                val tealColor = ContextCompat.getColor(binding.root.context, R.color.brand_teal_text)
                spannableString.setSpan(ForegroundColorSpan(tealColor), startIdx, endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(StyleSpan(Typeface.BOLD), startIdx, endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            binding.tvSlideTitle.text = spannableString

            if(slide.isFinalSlide){
                binding.tvSkip.visibility = View.GONE
                binding.btnGetStarted.visibility = View.VISIBLE
                binding.tvAlreadyHaveAccount.visibility = View.VISIBLE

                val loginPrompt = "Already have an account? Log In"
                val loginSpannable = SpannableString(loginPrompt)
                val logInStart = loginPrompt.indexOf("Log In")

                if (logInStart != -1) {
                    val tealColor = ContextCompat.getColor(binding.root.context, R.color.brand_teal_text)
                    loginSpannable.setSpan(ForegroundColorSpan(tealColor), logInStart, logInStart + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    loginSpannable.setSpan(StyleSpan(Typeface.BOLD), logInStart, logInStart + 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                binding.tvAlreadyHaveAccount.text = loginSpannable
            }else{
                binding.tvSkip.visibility = View.VISIBLE
                binding.btnGetStarted.visibility = View.GONE
                binding.tvAlreadyHaveAccount.visibility = View.GONE
            }

            binding.btnGetStarted.setOnClickListener { onGetStartedClick() }
            binding.tvAlreadyHaveAccount.setOnClickListener { onLogInClick() }
            binding.tvSkip.setOnClickListener { onSkipClick() }
        }
    }
}