package com.example.tseaafricaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class for the RecyclerView
class CookwareAdapter(private val cookwareList: List<String>) :
    RecyclerView.Adapter<CookwareAdapter.CookwareViewHolder>() {

    class CookwareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCookwareItem: TextView = itemView.findViewById(R.id.txtCookwareItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookwareViewHolder {
        // Inflate the item layout for the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cookware, parent, false)
        return CookwareViewHolder(view)
    }

    override fun onBindViewHolder(holder: CookwareViewHolder, position: Int) {
        // Bind the cookware item to the TextView
        holder.txtCookwareItem.text = cookwareList[position]
    }

    override fun getItemCount(): Int {
        return cookwareList.size
    }
}
