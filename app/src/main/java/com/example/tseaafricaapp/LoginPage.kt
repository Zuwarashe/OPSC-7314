package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executor
import java.util.regex.Pattern

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val RC_SIGN_IN = 9001 // Request code for Google sign-in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        firebaseManager = FirebaseManager(this)
        auth = FirebaseAuth.getInstance()

        // Initialize Biometric Authentication setup
        setupBiometricAuthentication()

        // Set up Google Sign-In client
        googleSignInClient = GoogleSignIn.getClient(this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

        findViewById<Button>(R.id.bioBtn).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

// Regular login with email and password
        findViewById<Button>(R.id.createAccBtn).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailTxt).text.toString()
            val password = findViewById<TextInputEditText>(R.id.passwordTxt).text.toString()

            if (validateEmail(email)) {
                firebaseManager.signInWithEmail(email, password) { success, errorMessage ->
                    if (success) {
                        checkUserExists(auth.currentUser)
                    } else {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Check email and password requirements.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.linktxt3).setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }
    }

    // Initialize biometric authentication prompt once in onCreate
    private fun setupBiometricAuthentication() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(this)
                biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // Show a success toast message
                        Toast.makeText(this@LoginPage, "Authentication successful", Toast.LENGTH_SHORT).show()
                        // Proceed to the Home page
                        startActivity(Intent(this@LoginPage, Home::class.java))
                        finish() // Close the LoginPage activity
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@LoginPage, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginPage, Home::class.java))
                        finish() // Close the LoginPage activity
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(this@LoginPage, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginPage, Home::class.java))
                        finish() // Close the LoginPage activity
                    }
                })

                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Login")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Cancel")
                    .build()
            }

            // Handle other biometric cases as you've already done
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "Device does not support biometric authentication.", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric hardware is currently unavailable.", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No biometrics enrolled. Please set up biometrics in your device settings.", Toast.LENGTH_LONG).show()
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                startActivity(enrollIntent)
            }

            else -> {
                Toast.makeText(this, "Biometric authentication is not available.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // No need to check user existence, just navigate to Home
                startActivity(Intent(this, Home::class.java))
                finish()
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com$").matcher(email).matches()) {
            true
        } else {
            Toast.makeText(this, "Email must be in the format example@example.com", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun checkUserExists(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, Home::class.java))
            finish()
        } else {
            Toast.makeText(this, "User account does not exist. Please sign up.", Toast.LENGTH_SHORT).show()
        }
    }
}