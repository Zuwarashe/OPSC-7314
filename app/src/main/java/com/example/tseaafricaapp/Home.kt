package com.example.tseaafricaapp

import RecipeAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

//------------------Fetch Recipes from Firebase in the Home Activity
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipesList: MutableList<Recipe> = mutableListOf()

//=================END : Fetch Recipes from Firebase in the Home Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

///----------Fetch Recipes from Firebase in the Home Activity
        recipeRecyclerView = findViewById(R.id.recCreatedRecView)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        fetchRecipesFromDatabase()
        fetchPublicRecipes()
///-----------END: Read Recipe from database


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

    private fun updateRecyclerView() {
        recipeAdapter = RecipeAdapter(recipesList.distinct())
        recipeRecyclerView.adapter = recipeAdapter
    }

    private fun fetchPublicRecipes() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                for (userSnapshot in snapshot.children){
                    for (recipeSnapshot in userSnapshot.children){
                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        recipe?.let{
                            if (it.isPublic == true) {
                                recipesList.add(it)
                            }
                        }
                    }
                }
                updateRecyclerView()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    //------------Fetch Recipes from Firebase in the Home ActivitY
    private fun fetchRecipesFromDatabase() {
        val userId = auth.currentUser?.uid ?: return
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes").child(userId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
            recipesList.clear()
            for (recipeSnapshot in snapshot.children) {
                val recipe = recipeSnapshot.getValue(Recipe::class.java)
                recipe?.let { recipesList.add(it) }
            }
            recipeAdapter = RecipeAdapter(recipesList)
            recipeRecyclerView.adapter = recipeAdapter
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
        })
    }
//============END: Fetch Recipes from Firebase in the Home ActivitY

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