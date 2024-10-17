package com.example.tseaafricaapp.api

import com.example.tseaafricaapp.api.SpoonacularApiService
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.spoonacular.com"
    private const val API_KEY = ""  // Replace with your actual API key

    // Create the OkHttpClient with the interceptor to add the API key
    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header("x-api-key", API_KEY)
                .build()
            chain.proceed(request)
        })
        .build()

    // Create the Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // Set the OkHttpClient
        .addConverterFactory(MoshiConverterFactory.create()) // Using Moshi for JSON conversion
        .build()

    // Function to create the SpoonacularApiService
    fun create(): SpoonacularApiService {
        return retrofit.create(SpoonacularApiService::class.java)
    }
}
