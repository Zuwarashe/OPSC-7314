package com.example.tseaafricaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientAdapter (private val ingredientList: MutableList<IngredientInput.Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>()  {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val txtMeasurement: TextView = itemView.findViewById(R.id.txtMeasurement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.txtName.text = ingredient.name
        holder.txtQuantity.text = ingredient.quantity
        holder.txtMeasurement.text = ingredient.measurement
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
    fun updateIngredients(newIngredientList: List<IngredientInput.Ingredient>) {
        ingredientList.clear() // Clear the current list
        ingredientList.addAll(newIngredientList) // Add the new ingredients
        notifyDataSetChanged() // Notify the adapter about the change
    }

}