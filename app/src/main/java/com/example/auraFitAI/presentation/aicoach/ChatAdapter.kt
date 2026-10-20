package com.example.auraFitAI.presentation.aicoach

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.auraFitAI.R
import com.example.auraFitAI.databinding.ItemChatMessageBinding

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        val context = holder.itemView.context

        holder.binding.tvMessageText.text = message.text

        val params = holder.binding.containerMessage.layoutParams as ViewGroup.MarginLayoutParams
        if (message.isUser) {
            params.marginStart = 80
            params.marginEnd = 16
            holder.binding.containerMessage.gravity = Gravity.END
            holder.binding.tvMessageText.background = ContextCompat.getDrawable(context, R.drawable.bg_button_gradient)
            holder.binding.tvMessageText.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        } else {
            params.marginStart = 16
            params.marginEnd = 80
            holder.binding.containerMessage.gravity = Gravity.START
            holder.binding.tvMessageText.background = ContextCompat.getDrawable(context, R.drawable.item_circular_color_gray_background)
            holder.binding.tvMessageText.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        }
        holder.binding.containerMessage.layoutParams = params
    }

    override fun getItemCount(): Int = messages.size
}
