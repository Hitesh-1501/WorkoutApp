package com.example.auraFitAI.presentation.aicoach

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.FragmentAiCoachBinding
import com.example.auraFitAI.presentation.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiCoachFragment : Fragment(R.layout.fragment_ai_coach) {
    private val binding by viewBinding(FragmentAiCoachBinding::bind)
    private val chatList = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChat()

        binding.cardPrompt1.setOnClickListener {
            sendUserMessage("Suggest a 10-minute core workout")
        }

        binding.cardPrompt2.setOnClickListener {
            sendUserMessage("How to improve pushup form?")
        }

        binding.btnSend.setOnClickListener {
            val text = binding.etMessageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                sendUserMessage(text)
                binding.etMessageInput.setText("")
            }
        }
    }

    private fun setupChat() {
        chatList.add(ChatMessage("Hello athlete! I'm your AuraFit AI Coach. Ask me anything about workouts, nutrition, or fitness routines!", false))

        chatAdapter = ChatAdapter(chatList)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.rvChatMessages.layoutManager = layoutManager
        binding.rvChatMessages.adapter = chatAdapter
    }

    private fun sendUserMessage(text: String) {
        chatList.add(ChatMessage(text, true))
        chatAdapter.notifyItemInserted(chatList.size - 1)
        binding.rvChatMessages.scrollToPosition(chatList.size - 1)

        generateAiResponse(text)
    }

    private fun generateAiResponse(query: String) {
        val lower = query.lowercase()
        val response = when {
            lower.contains("core") || lower.contains("abs") -> 
                "Here is your 10-Min Core Crusher:\n1. Plank (60s)\n2. Bicycle Crunches (45s)\n3. Russian Twists (45s)\n4. Mountain Climbers (45s)\n\nMaintain steady breathing and keep your core tight!"
            lower.contains("pushup") || lower.contains("form") -> 
                "Pushup Form Tips:\n• Keep body in a straight line\n• Elbows tucked at 45°\n• Lower chest just above ground\n• Engage glutes & core."
            lower.contains("diet") || lower.contains("nutrition" ) || lower.contains("food") -> 
                "Fuel your workouts with lean proteins, complex carbs, and stay hydrated with at least 3L of water daily!"
            else -> 
                "Great question! Consistency is key in fitness. Focus on proper form, progressive overload, and getting 7-8 hours of quality sleep."
        }

        binding.rvChatMessages.postDelayed({
            chatList.add(ChatMessage(response, false))
            chatAdapter.notifyItemInserted(chatList.size - 1)
            binding.rvChatMessages.scrollToPosition(chatList.size - 1)
        }, 500)
    }
}
