package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.provider.Settings // Add this import

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executor
import java.util.regex.Pattern

class LoginPage : AppCompatActivity() {
    // Firebase authentication and Google sign-in setup
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001 // Request code for Google sign-in

    // Variables for biometric authentication
    private lateinit var fingerprint: ImageView
    private var REQUEST_CODE = 9002;
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    //private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        // Initialize Firebase and biometric manager
        firebaseManager = FirebaseManager(this)
        auth = FirebaseAuth.getInstance()

        val bioBtn: Button = findViewById(R.id.bioBtn)
        // Initialize the fingerprint ImageView
        fingerprint = findViewById(R.id.fingerprint)


        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is signed in, proceed to Home activity
            startActivity(Intent(this, Home::class.java))
            finish()  // Prevents returning to login page on back press
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val gmailBtn = findViewById<Button>(R.id.gmailBtn)

        gmailBtn.setOnClickListener {
            signIn()
        }
        val emailTxt = findViewById<EditText>(R.id.emailTxt)
        val passwordTxt = findViewById<TextInputEditText>(R.id.passwordTxt)
        val loginBtn = findViewById<Button>(R.id.createAccBtn)
        val linkTxt3 = findViewById<TextView>(R.id.linktxt3)

        loginBtn.setOnClickListener {
            val email = emailTxt.text.toString()
            val password = passwordTxt.text.toString()
            if (validateEmail(email) && password.isNotEmpty()) {
                val firebaseManager = FirebaseManager(this)
                firebaseManager.signInWithEmail(email, password) { success, errorMessage ->
                    if (success) {
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        linkTxt3.setOnClickListener {
            // Navigate to the Signup page
            val intent = Intent(this, RegisterPage::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        // Setup for biometric authentication
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "No biometric features available on this device.", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric features are currently unavailable.", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }

        // Initialize executor
        executor = ContextCompat.getMainExecutor(this)



        // Configure biometric prompt for login
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val firebaseManager = FirebaseManager(this@LoginPage)
                    // Example data; replace with actual biometric data.
                    val fingerprintData = mapOf("authType" to "fingerprint")
                    val faceData = mapOf("authType" to "face")

                    firebaseManager.saveBiometricData(userId, fingerprintData, faceData) { success, error ->
                        if (success) {
                            startActivity(Intent(this@LoginPage, Home::class.java))
                            finish() // Prevent going back to login page
                        } else {
                            Toast.makeText(this@LoginPage, "Biometric data save failed: $error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@LoginPage, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show()
            }
            })

        // Set up biometric login button
        bioBtn.setOnClickListener {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }



    private fun checkUserExists(user: FirebaseUser?) {
        if (user != null) {
            // User exists, proceed to Home activity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        } else {
            // User does not exist
            Toast.makeText(this, "User account does not exist. Please sign up.", Toast.LENGTH_SHORT).show()
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
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    checkUserExists(user)
                    // User is signed in, navigate to the next activity
                    //val intent = Intent(this, Home::class.java)
                    //startActivity(intent)
                    //finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
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

    private fun retrieveBiometricData(userId: String) {
        firebaseManager.getBiometricData(userId) { biometricData, error ->
            if (biometricData != null) {
                // Handle or display biometric data
                Log.d("LoginPage", "Biometric data retrieved: $biometricData")
            } else {
                Toast.makeText(this, "No biometric data found: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

}


//    private fun validatePassword(password: String): Boolean {
//        val passwordPattern = Pattern.compile(
//            "^(?=.[A-Z])(?=.[a-z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{12,}$"
//        )
//        return if (passwordPattern.matcher(password).matches()) {
//            true
//        } else {
//            Toast.makeText(
//                this,
//                "Password must be at least 12 characters long, contain uppercase and lowercase letters, a number, and a special character",
//                Toast.LENGTH_LONG
//            ).show()
//            false
//        }
//    }

//    private fun loginUser(email: String, password: String) {
//        firebaseManager.signInWithEmail(email, password) { success, error ->
//            if (success) {
//                val userId = firebaseManager.getCurrentUser()?.uid
//                if (userId != null) {
//                    retrieveBiometricData(userId)
//                }
//            } else {
//                Toast.makeText(this, "Login failed: $error", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
