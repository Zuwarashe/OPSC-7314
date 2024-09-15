package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        // Enable Edge-to-Edge
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        val nameTxt = findViewById<EditText>(R.id.nameTxt)
        val emailTxt = findViewById<EditText>(R.id.emailTxt)
        val passwordTxt = findViewById<TextInputEditText>(R.id.passwordTxt)
        val createAccBtn = findViewById<Button>(R.id.createAccBtn)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val linktxt2 = findViewById<TextView>(R.id.linktxt2)

        createAccBtn.setOnClickListener {
            // Retrieve user inputs
            val fullName = nameTxt.text.toString()
            val email = emailTxt.text.toString()
            val password = passwordTxt.text.toString()

            // Validate inputs
            if (validateFullName(fullName) && validateEmail(email) && validatePassword(password)) {
                if (checkBox.isChecked) {
                    // Navigate to Home page
                    val intent = Intent(this, PersonalisedMeals::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Please agree to TseaAfrica's Terms and Conditions", Toast.LENGTH_SHORT).show()
                }
            }
        }

        linktxt2.setOnClickListener {
            // Navigate to Login page
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    private fun validateFullName(fullName: String): Boolean {
        return if (fullName.isNotEmpty() && fullName.all { it.isLetter() || it.isWhitespace() }) {
            true
        } else {
            Toast.makeText(this, "Full name must contain only letters and spaces", Toast.LENGTH_SHORT).show()
            false
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