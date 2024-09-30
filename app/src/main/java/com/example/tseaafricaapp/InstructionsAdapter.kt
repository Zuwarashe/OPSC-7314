package com.example.tseaafricaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InstructionsAdapter (private val instructionsList: List<String>) : RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder>() {

    class InstructionsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val instructionStep: TextView = view.findViewById(R.id.tvInstruction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instructions, parent, false)
        return InstructionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionsViewHolder, position: Int) {
        val instructions = instructionsList[position]
        holder.instructionStep.text = instructions
    }

    override fun getItemCount() = instructionsList.size
}
