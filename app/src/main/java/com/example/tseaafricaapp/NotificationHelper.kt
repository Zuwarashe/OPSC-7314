package com.example.tseaafricaapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

class NotificationHelper(private val context: Context) {
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "public_recipes",
                "Public Recipes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new public recipes"
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}