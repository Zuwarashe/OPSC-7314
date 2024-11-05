package com.example.tseaafricaapp

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.InputStream
import java.io.IOException

class FCMService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "FCMService"
        private const val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
        private const val FCM_API_URL = "https://fcm.googleapis.com/v1/projects/YOUR_PROJECT_ID/messages:send"
    }

    private fun getAccessToken(): String {
        val stream: InputStream = assets.open("service-account.json")
        val credentials = GoogleCredentials
            .fromStream(stream)
            .createScoped(listOf(MESSAGING_SCOPE))
        credentials.refresh()
        return credentials.accessToken.tokenValue
    }

    fun sendPublicRecipeNotification(recipeName: String, recipeId: String) {
        val token = getAccessToken()

        val message = JSONObject().apply {
            put("message", JSONObject().apply {
                // Target all users with the app installed
                put("topic", "public_recipes")

                // Notification payload
                put("notification", JSONObject().apply {
                    put("title", "Delicious Recipe Just Added! 🍽️")
                    put("body", "A new public recipe '$recipeName' has been added! Tap to explore.")
                })

                // Data payload
                put("data", JSONObject().apply {
                    put("recipe_id", recipeId)
                    put("click_action", "OPEN_RECIPE_DETAILS")
                })

                // Android specific configuration
                put("android", JSONObject().apply {
                    put("priority", "high")
                    put("notification", JSONObject().apply {
                        put("click_action", "OPEN_RECIPE_DETAILS")
                        put("channel_id", "public_recipes")
                    })
                })
            })
        }

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(FCM_API_URL)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .post(message.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send notification", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Notification sent successfully: ${response.body?.string()}")
                } else {
                    Log.e(TAG, "Notification failed with response code: ${response.code}")
                }
            }
        })

    }
}