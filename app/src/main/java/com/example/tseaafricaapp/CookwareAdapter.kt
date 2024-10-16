package com.example.tseaafricaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CookwareAdapter(private val cookwareList: List<String>) : RecyclerView.Adapter<CookwareAdapter.CookwareViewHolder>() {

    class CookwareViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cookwareItem: TextView = view.findViewById(R.id.tvCookware)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookwareViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cookware, parent, false)
        return CookwareViewHolder(view)
    }

    override fun onBindViewHolder(holder: CookwareViewHolder, position: Int) {
        val cookware = cookwareList[position]
        holder.cookwareItem.text = cookware
    }

    override fun getItemCount() = cookwareList.size
}
