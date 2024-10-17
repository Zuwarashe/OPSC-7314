package com.example.tseaafricaapp.api

import com.example.tseaafricaapp.model.Recipe

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/findByNutrients")
    fun searchRecipesByNutrients(
        @Query("minCarbs") minCarbs: Int,
        @Query("maxCarbs") maxCarbs: Int,
        @Query("minProtein") minProtein: Int,
        @Query("maxProtein") maxProtein: Int,
        @Query("minCalories") minCalories: Int,
        @Query("maxCalories") maxCalories: Int
    ): Call<List<Recipe>>
}
