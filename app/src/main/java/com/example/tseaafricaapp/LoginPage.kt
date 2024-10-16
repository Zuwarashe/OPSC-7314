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
import java.util.regex.Pattern

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

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
    }

    private fun signInWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    checkUserExists(user)
                } else {
                    Toast.makeText(this, "Authentication failed. Account does not exist.", Toast.LENGTH_SHORT).show()
                }
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