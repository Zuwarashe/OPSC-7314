package com.example.tseaafricaapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.os.Handler
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Transaction
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseManager(private val context: Context) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var biometricPrompt: BiometricPrompt

    //--------------------------Google Sign in
    fun initializeGoogleSignInClient(webClientId: String) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(data: Intent?, onResult: (Boolean, String?) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account, onResult)
        } catch (e: ApiException) {
            onResult(false, "Google sign-in failed: ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount?,
        onResult: (Boolean, String?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserDataToDatabase(user) { success ->
                        if (success) {
                            onResult(true, null)
                        } else {
                            onResult(false, "Failed to save user data to the database")
                        }
                    }
                } else {
                    onResult(false, "Authentication failed: ${task.exception?.message}")
                }
            }
    }

    //==========================END:Google Sign in
//------------------Sign up (email and password)
    fun signUpWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserDataToDatabase(user) { success ->
                        if (success) {
                            onResult(true, null)
                        } else {
                            onResult(false, "Failed to save user data to the database")
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    //==================END: Sign up (email and password)
//----------------Sign IN (email and password)
    fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserDataToDatabase(user) { success ->
                        if (success) {
                            onResult(true, null)
                        } else {
                            onResult(false, "Failed to save user data to the database")
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
//================END:Sign IN (email and password)

    //--------------------Save ID and users email into Realtime database
    private fun saveUserDataToDatabase(user: FirebaseUser?, onComplete: (Boolean) -> Unit) {
        val userId = user?.uid ?: return
        val userEmail = user.email ?: return

        val userData = mapOf("email" to userEmail)
        database.child("users").child(userId).setValue(userData)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    //====================END: Save ID and users email into Realtime database
//-------------------Cookware: Save Recipe to Firebase Realtime Database
    fun saveRecipeToDatabase(
        recipeName: String,
        totalMinutes: Int,
        totalServings: Int,
        cookwareList: List<String>,
        instructionList: List<String>,
        ingredientsList: List<String>,
        isPublic: Boolean,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val recipeId = database.child("recipes").push().key ?: return

        if (recipeName.isEmpty()) {
            onResult(false, "Recipe name is required.")
            return
        }
        val recipe = hashMapOf(
            "recipeId" to recipeId,
            "userId" to userId,
            "name" to recipeName,
            "totalMinutes" to totalMinutes,
            "totalServings" to totalServings,
            "isPublic" to isPublic,
            "cookware" to cookwareList,
            "instruction" to instructionList,
            "ingredients" to ingredientsList,
            "isFavorite" to false
        )
        database.child("recipes").child(userId).child(recipeId).setValue(recipe)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener {
                onResult(false, "Failed to save recipe.")
            }

    }
//===================END:Save Recipe to Firebase Realtime Database
//-------------------Manual Recipe


    fun saveMultipleRecipesToDatabase(
        recipes: List<Map<String, Any>>,
        onResult: (Boolean, String?) -> Unit
    ) {
        val publicRecipesRef = database.child("recipes").child("public")

        for (recipe in recipes) {
            val recipeId = publicRecipesRef.push().key ?: return

            publicRecipesRef.child(recipeId).setValue(recipe)
                .addOnSuccessListener {
                    onResult(true, null)
                }
                .addOnFailureListener {
                    onResult(false, "Failed to save recipe.")
                }
        }
    }
//==================END: Manual Recipe

    //----------------Other: not implemented
// Summary
    /*
        -Check if user is logged in
        -Sign out the current user
        -Get the currently logged in user
     */
    // Check if a user is logged in
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Sign out the current user
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    // Get the currently logged in user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
//=========================END: Other: not implemented


    // Add this method to retrieve biometric data
    fun getBiometricData(userId: String, onResult: (Map<String, Any>?, String?) -> Unit) {
        database.child("biometrics").child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val biometricData = snapshot.value as Map<String, Any>
                    onResult(biometricData, null)
                } else {
                    onResult(null, "No biometric data found.")
                }
            }
            .addOnFailureListener {
                onResult(null, "Failed to retrieve biometric data.")
            }
//-------------------END: Save and Retrieve Biometric Data
    }

    // Authenticate user with biometrics
    fun authenticateWithBiometrics(activity: FragmentActivity, onAuthenticationSuccess: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)
        biometricPrompt =
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(activity, "Authentication successful", Toast.LENGTH_SHORT).show()
                    onAuthenticationSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(activity, "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Authenticate using fingerprint or facial recognition")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    // Store the biometric data into Firebase
    fun saveBiometricData(userId: String, biometricIdentifier: String, callback: (Boolean, String?) -> Unit) {
        val userRef = database.child("biometric_data").child(userId)
        val biometricData = mapOf("biometricIdentifier" to biometricIdentifier)

        userRef.setValue(biometricData)
            .addOnSuccessListener { Log.d("FirebaseManager", "Biometric data saved successfully for user $userId")
                callback(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseManager", "Error saving biometric data: ${e.message}")
                callback(false, e.message)
            }
    }
}
