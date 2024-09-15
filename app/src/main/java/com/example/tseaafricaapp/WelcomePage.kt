package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomePage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_page)


        // Ensure edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the linktxt TextView
        val linkTextView = findViewById<TextView>(R.id.linktxt)

        // Set an OnClickListener on the linktxt TextView
        linkTextView.setOnClickListener {
            // Create an intent to start LoginPage activity
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        // Find the getStartedbtn Button
        val getStartedButton = findViewById<Button>(R.id.getStartedbtn)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }
}