package com.example.tseaafricaapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class IngredientInput : Fragment() {
    data class Ingredient(val name: String, val quantity: String, val measurement: String)


  //  private lateinit var ingredientAdapter: IngredientAdapter
    private val ingredientList = mutableListOf<Ingredient>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ingredient_input, container, false)
        val imageBtnClose: ImageButton = view.findViewById(R.id.imageBtnClose)

        val btnAddIngredient: Button = view.findViewById(R.id.btnAddIngredient)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewIngredient)

        val txtIngredient: EditText = view.findViewById(R.id.txtIngredient)
        val txtQuantity: EditText = view.findViewById(R.id.txtQuantity)
        val txtMeasurement: EditText = view.findViewById(R.id.txtMeasurement)

            //  ingredientAdapter = IngredientAdapter(ingredientList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
       // recyclerView.adapter = ingredientAdapter



        imageBtnClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        btnAddIngredient.setOnClickListener {
            val ingredientName = txtIngredient.text.toString().trim()
            val quantity = txtQuantity.text.toString().trim()
            val measurement = txtMeasurement.text.toString().trim()

            // Validate inputs
            if (ingredientName.isNotEmpty() && quantity.isNotEmpty() && measurement.isNotEmpty()) {
                // Add ingredient to list
                val ingredient = Ingredient(ingredientName, quantity, measurement)
                ingredientList.add(ingredient)

                Log.d("IngredientList", "Added: $ingredient")
                Log.d("IngredientList", "Current list: $ingredientList")

                // Notify adapter of data change
               // ingredientAdapter.notifyDataSetChanged()

                // Clear input fields
                txtIngredient.text.clear()
                txtQuantity.text.clear()
                txtMeasurement.text.clear()
            }
        }

        return view

    }

}