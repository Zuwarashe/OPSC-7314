package com.example.tseaafricaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter(private val ingredientsList: List<String>) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {


    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientItem: TextView = itemView.findViewById(R.id.tvIngredient) // Adjust based on your layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredients, parent, false) // Adjust your item layout
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.ingredientItem.text = ingredient
    }

    override fun getItemCount() = ingredientsList.size
}
