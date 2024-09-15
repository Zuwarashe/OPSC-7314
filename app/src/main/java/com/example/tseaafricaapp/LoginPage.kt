package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)  // Make sure to update with your actual layout file name

        // Initialize UI elements
        val emailTxt = findViewById<EditText>(R.id.emailTxt)
        val passwordTxt = findViewById<TextInputEditText>(R.id.passwordTxt)
        val loginBtn = findViewById<Button>(R.id.createAccBtn)
        val linkTxt3 = findViewById<TextView>(R.id.linktxt3)

        loginBtn.setOnClickListener {
            // Retrieve user inputs
            val email = emailTxt.text.toString()
            val password = passwordTxt.text.toString()

            // Validate inputs
            if (validateEmail(email) && validatePassword(password)) {
                // Perform login logic here (e.g., authenticate user)
                // Navigate to the PersonalisedMeals page
                val intent = Intent(this, PersonalisedMeals::class.java)
                startActivity(intent)
            }
        }

        linkTxt3.setOnClickListener {
            // Navigate to the Signup page
            val intent = Intent(this, RegisterPage::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (Pattern.compile(
                "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com$"
            ).matcher(email).matches()) {
            true
        } else {
            Toast.makeText(this, "Email must be in the format example@example.com", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun validatePassword(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$"
        )
        return if (passwordPattern.matcher(password).matches()) {
            true
        } else {
            Toast.makeText(
                this,
                "Password must be at least 12 characters long, contain uppercase and lowercase letters, a number, and a special character",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}