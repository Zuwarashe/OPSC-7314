package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
       val NextBtn = findViewById<Button>(R.id.Next_Button)


        NextBtn.setOnClickListener {
            // Navigate to the Signup page
            val intent = Intent(this, Settings::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

    }
}