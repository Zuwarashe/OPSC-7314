package com.example.tseaafricaapp.api

import com.example.tseaafricaapp.model.AnalyzeRecipeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SpoonacularApi {

    @POST("recipes/analyze")
    @Headers("Content-Type: application/json")
    fun analyzeRecipe(
        @Body request: AnalyzeRecipeRequest
    ): Call<Any>
}
