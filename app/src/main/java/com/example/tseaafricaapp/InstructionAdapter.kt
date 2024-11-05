package com.example.tseaafricaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InstructionAdapter(private val instructionList: List<String>) :
    RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return InstructionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val instruction = instructionList[position]
        holder.textView.text = instruction
    }

    override fun getItemCount(): Int {
        return instructionList.size
    }

    class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
}
