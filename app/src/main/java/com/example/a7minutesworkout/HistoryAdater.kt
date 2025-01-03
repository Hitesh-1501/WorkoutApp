package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemHistoryRowBinding

class HistoryAdater(private val items : ArrayList<String>) : RecyclerView.Adapter<HistoryAdater.ViewHolder>() {
    class ViewHolder(binding : ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val llHistory = binding.llHistoryItemMain
        val tvPosition = binding.tvPosition
        val tvItem = binding.tvItem

    }
    // Inflates the item views which is designed in xml layout file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false))
    }

    override fun getItemCount(): Int {
       return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : String  = items.get(position)
        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date
        if (position % 2 == 0) {
            holder.llHistory.setBackgroundColor(
                Color.parseColor("#EBEBEB")
            )
        }else{
            holder.llHistory.setBackgroundColor(
                Color.parseColor("#FFFFFF")
            )
        }
    }
}