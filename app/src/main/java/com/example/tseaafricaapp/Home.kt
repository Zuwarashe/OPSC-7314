package com.example.tseaafricaapp

import RecipeAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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

    //testing Manula recipe

    private lateinit var firebaseManager: FirebaseManager
    //end: testing Manula recipe

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    //------------------Fetch Recipes from Firebase in the Home Activity
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipesList: MutableList<Recipe> = mutableListOf()

    //=================END : Fetch Recipes from Firebase in the Home Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_home)

    //testing Manula recipe
    firebaseManager = FirebaseManager(this)
    //savePreMadeRecipes()


// Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance()

        // Set up the logout button functionality


    //end: testing Manula recipe

///----------Fetch Recipes from Firebase in the Home Activity

        recipeRecyclerView = findViewById(R.id.recCreatedRecView)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        auth = FirebaseAuth.getInstance()
        fetchRecipesFromDatabase()
        fetchPublicRecipes()
        fetchFavoriteRecipes()
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
                R.id.fave ->{
                    startActivity(Intent(applicationContext, Favourites::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.settings ->{
                    startActivity(Intent(applicationContext, Settings::class.java))
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


    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // If no user is logged in, redirect to the login page
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish() // To prevent returning to this activity
        }
    }
    private fun savePreMadeRecipes() {
        val premadeRecipes = listOf(
            mapOf(
                "name" to "Test Recipe 1",
                "totalMinutes" to 30,
                "totalServings" to 4,
                "cookware" to listOf("Pan", "Oven"),
                "instruction" to listOf("Preheat oven to 350°F", "Cook for 20 minutes"),
                "ingredients" to listOf("Chicken", "Rice", "Vegetables"),
                "isPublic" to true,
                "isFavorite" to false
            ),
            mapOf(
                "name" to "Test Recipe 2",
                "totalMinutes" to 45,
                "totalServings" to 2,
                "cookware" to listOf("Stove", "Pot"),
                "instruction" to listOf("Boil water", "Cook pasta for 10 minutes"),
                "ingredients" to listOf("Pasta", "Tomato sauce"),
                "isPublic" to true,
                "isFavorite" to false
            )
        )
        firebaseManager.saveMultipleRecipesToDatabase(premadeRecipes) { success, message ->
            if (success) {
                Toast.makeText(this, "All premade recipes saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save premade recipes: $message", Toast.LENGTH_SHORT).show()
            }
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
                            //(it.isPublic == true)
                            if (it.isPublic) {
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
    private fun fetchFavoriteRecipes() {
        val userId = auth.currentUser?.uid ?: return
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes").child(userId)

        // Query for recipes where isFavorite is true
        val query = databaseReference.orderByChild("isFavorite").equalTo(true)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favoriteRecipesList = mutableListOf<Recipe>()
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let { favoriteRecipesList.add(it) }  // Add to the favorite list
                }
                // Update the RecyclerView with the favorite recipes
                displayFavoriteRecipes(favoriteRecipesList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("HomePage", "Error fetching favorite recipes: ${error.message}")
            }})
    }

    private fun displayFavoriteRecipes(favoriteRecipesList: List<Recipe>) {
        val favoritesAdapter = RecipeAdapter(favoriteRecipesList)  // Use your existing RecipeAdapter
        val favoritesRecView = findViewById<RecyclerView>(R.id.favoritesRecView) // Ensure you have the correct reference
        favoritesRecView.adapter = favoritesAdapter
        favoritesRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}