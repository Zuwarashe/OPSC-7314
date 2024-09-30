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
            val intent = Intent(this, UserPrefence::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        yourRecipesLayout.setOnClickListener {
            val intent = Intent(this, Favourites::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        eatinPrefLayout.setOnClickListener {
            val intent = Intent(this, UserPrefence::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        meetOurTeamLayout.setOnClickListener {
            val intent = Intent(this, Team::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }
    }
}