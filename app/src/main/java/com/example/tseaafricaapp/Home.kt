package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Home : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

//------Manule adding recipe
        /*
        val recipeId = FirebaseDatabase.getInstance().reference.child("recipes").push().key

        val recipeData = mapOf(
            "recipeName" to "Injera",
            "liked" to true,
            "timeToCook" to "2 hours",
            "servings" to 6,
            "cookware" to "pan",
            "ingredients" to listOf(
                mapOf("name" to "teff flour", "quantity" to "3 cups"),
                mapOf("name" to "water", "quantity" to "4 cups"),
                mapOf("name" to "salt", "quantity" to "1 tsp")
            ),
            "instructions" to "Mix teff flour and water, ferment for 2 days...",
            "imageURL" to "",
            "public" to true,
            "ownerId" to FirebaseAuth.getInstance().currentUser?.uid
        )


        FirebaseDatabase.getInstance().reference.child("recipes").child(recipeId!!).setValue(recipeData)
        */
//---------END: Manule adding recipe
        /*
                //added for log out
                // Initialize Firebase Auth
                auth = FirebaseAuth.getInstance()
                // Initialize GoogleSignInClient
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
                googleSignInClient = GoogleSignIn.getClient(this, gso)
                //added for log out end
                val sign_out_button = findViewById<Button>(R.id.logout_button)
                sign_out_button.setOnClickListener {
                    signOutAndStartSignInActivity()
                }
                */

///------------------------Recipe database

///------------------------ Recipe database  end
///--------------Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.home

        bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.home -> true
                R.id.mealPlan ->{
                    startActivity(Intent(applicationContext, Cookware::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }
///--------------Navigation end
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun signOutAndStartSignInActivity() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}