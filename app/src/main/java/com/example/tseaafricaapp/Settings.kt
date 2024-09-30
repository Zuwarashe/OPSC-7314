package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        // Get the root view of each included layout and set click listeners
        val healthGoalLayout = findViewById<LinearLayout>(R.id.health_goal_btn)
        val yourRecipesLayout = findViewById<LinearLayout>(R.id.your_recipes_btn)
        val eatinPrefLayout = findViewById<LinearLayout>(R.id.eatin_pref_btn)
        val meetOurTeamLayout = findViewById<LinearLayout>(R.id.meet_our_team_btn)

        healthGoalLayout.setOnClickListener {
            // Handle click event
            Toast.makeText(this, "Health Goal clicked", Toast.LENGTH_SHORT).show()
            // Or start a new activity, navigate, etc.
        }

        yourRecipesLayout.setOnClickListener {
            val intent = Intent(this, Favourites::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        eatinPrefLayout.setOnClickListener {
            Toast.makeText(this, "Eatin Pref clicked", Toast.LENGTH_SHORT).show()
        }

        meetOurTeamLayout.setOnClickListener {
            Toast.makeText(this, "Meet Our Team clicked", Toast.LENGTH_SHORT).show()
        }
    }
}