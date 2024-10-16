package com.example.tseaafricaapp

import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsAnimationCompat
import com.example.tseaafricaapp.api.ApiClient
import com.example.tseaafricaapp.api.SpoonacularApiService
import com.example.tseaafricaapp.model.Recipe
import com.google.android.gms.common.api.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPrefence : AppCompatActivity() {
    private lateinit var tvResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_prefence)

        val api = ApiClient.create()

        // Initialize UI elements
        val minCarbsEditText: EditText = findViewById(R.id.minCarbs)
        val maxCarbsEditText: EditText = findViewById(R.id.maxCarbs)
        val minProteinEditText: EditText = findViewById(R.id.minProtein)
        val maxProteinEditText: EditText = findViewById(R.id.maxProtein)
        val minCaloriesEditText: EditText = findViewById(R.id.minCalories)
        val maxCaloriesEditText: EditText = findViewById(R.id.maxCalories)
        val searchButton: Button = findViewById(R.id.btnSearch)
        tvResults = findViewById(R.id.tvResults)




        searchButton.setOnClickListener {
            // Collect user inputs
            val minCarbs = minCarbsEditText.text.toString().toIntOrNull() ?: 0
            val maxCarbs = maxCarbsEditText.text.toString().toIntOrNull() ?: 100
            val minProtein = minProteinEditText.text.toString().toIntOrNull() ?: 0
            val maxProtein = maxProteinEditText.text.toString().toIntOrNull() ?: 100
            val minCalories = minCaloriesEditText.text.toString().toIntOrNull() ?: 50
            val maxCalories = maxCaloriesEditText.text.toString().toIntOrNull() ?: 800

            // Make the API call
            searchRecipesByNutrients(api, minCarbs, maxCarbs, minProtein, maxProtein, minCalories, maxCalories)
        }
    }

    private fun searchRecipesByNutrients(
        api: SpoonacularApiService,
        minCarbs: Int,
        maxCarbs: Int,
        minProtein: Int,
        maxProtein: Int,
        minCalories: Int,
        maxCalories: Int
    ) {
        api.searchRecipesByNutrients(minCarbs, maxCarbs, minProtein, maxProtein, minCalories, maxCalories)
            .enqueue(object : WindowInsetsAnimationCompat.Callback<List<Recipe>> {
                override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                    if (response.isSuccessful) {
                        val recipes = response.body()
                        tvResults.text = formatRecipes(recipes)
                    } else {
                        tvResults.text = "Error: ${response.code}"
                    }
                }

                override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                    tvResults.text = "Failed: ${t.message}"
                }
            })
    }

    private fun formatRecipes(recipes: List<Recipe>?): String {
        if (recipes.isNullOrEmpty()) return "No recipes found"
        return recipes.joinToString("\n\n") { recipe ->
            "Title: ${recipe.title}\nCalories: ${recipe.calories} kcal\nCarbs: ${recipe.carbs}g\nProtein: ${recipe.protein}g\nFat: ${recipe.fat}g"
        }
    }
}
