package com.example.tseaafricaapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.util.regex.Pattern

class RegisterPage : AppCompatActivity() {

    /*
    companion object {
        private const val RC_SIGN_IN = 9001
    }
    */
    private val RC_SIGN_IN = 9001
    private lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        firebaseManager = FirebaseManager(this)
        firebaseManager.initializeGoogleSignInClient("YOUR_WEB_CLIENT_ID")

        val signInButton = findViewById<Button>(R.id.gmailBtn)
        signInButton.setOnClickListener {
            val signInIntent = firebaseManager.getGoogleSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


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

            firebaseManager.signUpWithEmail(email, password) { success, message ->
                if (success) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to the next activity
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Registration failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        linktxt2.setOnClickListener {
            // Navigate to Login page
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            firebaseManager.handleGoogleSignInResult(data) { success, message ->
                if (success) {
                    Toast.makeText(this, "Google sign-in successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Google sign-in failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}