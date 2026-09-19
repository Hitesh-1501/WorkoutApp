package com.example.auraFitAI.presentation.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SessionManager {
    private const val PREF_NAME = "aura_fit_session"
    private const val KEY_ONBOARDING_COMPLETED_PREFIX = "onboarding_completed_"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isOnboardingCompleted(context: Context, uid: String): Boolean {
        return getPrefs(context).getBoolean("$KEY_ONBOARDING_COMPLETED_PREFIX$uid", false)
    }

    fun setOnboardingCompleted(context: Context, uid: String, completed: Boolean) {
        getPrefs(context).edit { putBoolean("$KEY_ONBOARDING_COMPLETED_PREFIX$uid", completed) }
    }
}
