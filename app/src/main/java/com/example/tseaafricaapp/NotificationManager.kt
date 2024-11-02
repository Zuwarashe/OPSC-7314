package com.example.tseaafricaapp


import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class NotificationManager(private val context: Context) {

    fun subscribeToTopic(topic: String, onComplete: (Boolean) -> Unit) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun unsubscribeFromTopic(topic: String, onComplete: (Boolean) -> Unit) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun getDeviceToken(onComplete: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onComplete(null)
                    return@OnCompleteListener
                }
                onComplete(task.result)
            })
    }
}