import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class RecipeNotificationManager {
    companion object {
        private const val FCM_API = "https://fcm.googleapis.com/v1/projects/tseaafricadb-532c4/messages:send"
        private const val SERVER_KEY = "your_actual_server_key_here"

        fun sendNewRecipeNotification(recipeName: String) {
            Thread {
                try {
                    val url = URL(FCM_API)
                    val conn = url.openConnection() as HttpURLConnection

                    conn.apply {
                        useCaches = false
                        doInput = true
                        doOutput = true
                        requestMethod = "POST"
                        setRequestProperty("Authorization", "Bearer $SERVER_KEY")
                        setRequestProperty("Content-Type", "application/json")
                    }

                    // Create notification payload
                    val notification = JSONObject().apply {
                        put("title", "New Recipe Available!")
                        put("body", "Someone shared a new recipe: $recipeName")
                    }

                    val fcmMessage = JSONObject().apply {
                        put("notification", notification)
                        put("topic", "public_recipes")
                    }

                    val jsonRequest = JSONObject().apply {
                        put("message", fcmMessage)
                    }

                    // Send request
                    val wr = OutputStreamWriter(conn.outputStream)
                    wr.write(jsonRequest.toString())
                    wr.flush()

                    // Check response
                    val responseCode = conn.responseCode
                    if (responseCode != 200) {
                        throw Exception("FCM notification failed: $responseCode")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }

        fun subscribeToPublicRecipes() {
            FirebaseMessaging.getInstance().subscribeToTopic("public_recipes")
        }
    }
}