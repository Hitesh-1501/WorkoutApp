package com.example.auraFitAI.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auraFitAI.databinding.ItemHomeWorkoutBinding

class HomeWorkoutAdapter(
    private val items: List<HomeWorkoutItem>,
    private val onItemClicked: (HomeWorkoutItem) -> Unit
) : RecyclerView.Adapter<HomeWorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(private val binding: ItemHomeWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeWorkoutItem) {
            binding.tvCardTitle.text = item.title
            binding.tvCardSubtitle.text = item.subtitle
            binding.tvCardDuration.text = item.duration
            binding.ivCardIcon.setImageResource(item.iconRes)
            binding.root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemHomeWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
