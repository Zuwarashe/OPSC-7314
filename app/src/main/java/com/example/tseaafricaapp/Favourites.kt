package com.example.tseaafricaapp

import RecipeAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Favourites : AppCompatActivity() {

    private lateinit var firebaseManager: FirebaseManager

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)  // Call setContentView first to inflate the layout

        // Find the main layout view by ID
        val mainView = findViewById<View>(R.id.main)

        // Apply window insets only if the view is not null
        mainView?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                // Get system bar insets (status bar, navigation bar, etc.)
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                // Apply padding to the view to accommodate system bars
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        firebaseManager = FirebaseManager(this)

///----------Fetch Recipes from Firebase in the Home Activity

        /*recipeRecyclerView = findViewById(R.id.recCreatedRecView)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
*/
        auth = FirebaseAuth.getInstance()
        //fetchRecipesFromDatabase()

        fetchFavoriteRecipes()

        ///--------------Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.fave

        bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.fave -> true
                R.id.home ->{
                    startActivity(Intent(applicationContext, Home::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.mealPlan ->{
                    startActivity(Intent(applicationContext, Cookware::class.java))
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
    }
///--------------Favourites start

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
        favoritesRecView.layoutManager = LinearLayoutManager(this)
    }
}
