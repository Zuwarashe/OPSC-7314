package com.example.tseaafricaapp

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class Recipe(
    @get:PropertyName("recipeId") @set:PropertyName("recipeId") var recipeId: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("totalMinutes") @set:PropertyName("totalMinutes") var totalMinutes: Int = 0,
    @get:PropertyName("totalServings") @set:PropertyName("totalServings") var totalServings: Int = 0,
    @get:PropertyName("isPublic") @set:PropertyName("isPublic") var isPublic: Boolean = false,
    @get:PropertyName("isFavorite") @set:PropertyName("isFavorite") var isFavorite: Boolean = false,
    @get:PropertyName("cookware") @set:PropertyName("cookware") var cookware: List<String> = listOf(),
    @get:PropertyName("ingredients") @set:PropertyName("ingredients") var ingredients: List<String> = listOf(),
    @get:PropertyName("instruction") @set:PropertyName("instruction") var instruction: List<String> = listOf(),
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl: String = ""
)



