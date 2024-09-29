package com.example.tseaafricaapp

data class Recipe(
    val recipeId: String = "",              // Unique ID for the recipe
    val userId: String = "",                // ID of the user who created the recipe
    val name: String = "",                   // Name of the recipe
    val totalMinutes: Int = 0,               // Total time to prepare the recipe (in minutes)
    val totalServings: Int = 0,              // Number of servings the recipe makes
    val isPublic: Boolean = false,            // Boolean indicating if the recipe is public
    val cookware: List<String> = emptyList(), // List of cookware needed for the recipe
    val ingredients: List<String> = emptyList(), // List of ingredients needed for the recipe
    val instructions: List<String> = emptyList(), // Step-by-step instructions for the recipe
    val isFavorite: Boolean = false           // Boolean indicating if the recipe is marked as favorite
)
