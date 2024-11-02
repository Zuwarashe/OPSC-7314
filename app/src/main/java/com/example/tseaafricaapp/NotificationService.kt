package com.example.tseaafricaapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.tseaafricaapp.R
import com.example.tseaafricaapp.MainActivity

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send this token to your server
        sendRegistrationTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle notification when app is in foreground
        remoteMessage.notification?.let { notification ->
            createNotificationChannel()
            showNotification(notification.title ?: "New Recipe Alert",
                notification.body ?: "Check out what's cooking!")
        }

        // Handle data payload
        remoteMessage.data.isNotEmpty().let {
            // Process data payload
            handleDataPayload(remoteMessage.data)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "recipe_notifications"
            val channelName = "Recipe Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notifications for new recipes and updates"
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "recipe_notifications")
            .setSmallIcon(R.drawable.notifications) // Make sure to create this icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun handleDataPayload(data: Map<String, String>) {
        // Handle different types of notifications based on data
        when (data["type"]) {
            "new_recipe" -> handleNewRecipeNotification(data)
            "recipe_update" -> handleRecipeUpdateNotification(data)
            "favorite" -> handleFavoriteNotification(data)
            else -> handleDefaultNotification(data)
        }
    }

    private fun handleNewRecipeNotification(data: Map<String, String>) {
        showNotification(
            "New Recipe Added!",
            "Check out the new recipe: ${data["recipe_name"]}"
        )
    }

    private fun handleRecipeUpdateNotification(data: Map<String, String>) {
        showNotification(
            "Recipe Updated",
            "The recipe '${data["recipe_name"]}' has been updated"
        )
    }

    private fun handleFavoriteNotification(data: Map<String, String>) {
        showNotification(
            "New Favorite!",
            "Someone liked your recipe: ${data["recipe_name"]}"
        )
    }

    private fun handleDefaultNotification(data: Map<String, String>) {
        showNotification(
            data["title"] ?: "New Notification",
            data["message"] ?: "You have a new notification"
        )
    }

    private fun sendRegistrationTokenToServer(token: String) {
        // Implement the logic to send the token to your backend server
        // This is important for targeting specific devices for notifications
    }
}