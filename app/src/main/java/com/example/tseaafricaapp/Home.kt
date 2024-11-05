package com.example.tseaafricaapp

import RecipeAdapter
import android.app.NotificationManager
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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class Home : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var firebaseManager: FirebaseManager

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

//-------------------explore
    private lateinit var exploreAdapter: RecipeAdapter
    private val exploreRecipesList = mutableListOf<Recipe>()

    //private lateinit var exploreRecyclerView: RecyclerView
    private lateinit var exploreRecipeAdapter: RecipeAdapter
    //private val exploreRecipeList: MutableList<Recipe> = mutableListOf()
//===================END:  explore

//------------------Fetch Recipes from Firebase in the Home Activity
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipesList: MutableList<Recipe> = mutableListOf()

//=================END : Fetch Recipes from Firebase in the Home Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)


    firebaseManager = FirebaseManager(this)



//--------------------    explore
    val exploreRecView = findViewById<RecyclerView>(R.id.exploreRecView)
    exploreRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    exploreAdapter = RecipeAdapter(exploreRecipesList)
    exploreRecView.adapter = exploreAdapter


    //fetchPublicRecipes()
    fetchExploreRecipes()
//==========================END: explore

    //end: testing Manula recipe

///----------Fetch Recipes from Firebase in the Home Activity

        recipeRecyclerView = findViewById(R.id.recCreatedRecView)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        auth = FirebaseAuth.getInstance()
        fetchRecipesFromDatabase()
    fetchFavoriteRecipes()


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

    private fun fetchExploreRecipes() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("recipes")
        exploreRecipesList.clear()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                exploreRecipesList.clear()
                for (userSnapshot in snapshot.children) {
                    for (recipeSnapshot in userSnapshot.children) {
                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        if (recipe != null) {
                            Log.d("fetchExploreRecipes", "Fetched recipe: ${recipe.name}, isPublic: ${recipe.isPublic}")
                        }
                        if (recipe?.isPublic == true) {
                            exploreRecipesList.add(recipe)
                        }
                    }
                }
                Log.d("fetchExploreRecipes", "Total public recipes fetched: ${exploreRecipesList.size}")
                exploreAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("fetchExploreRecipes", "Error fetching explore recipes: ${error.message}")
            }
        })
    }





    private fun savePreMadeRecipes() {
        val premadeRecipes = listOf(
            mapOf(
                "name" to "West African Peanut Stew",
                "totalMinutes" to 45,
                "totalServings" to 4,
                "cookware" to listOf("Large pot"),
                "instruction" to listOf("Preheat oven to 350°F", "Cook for 20 minutes"),
                "ingredients" to listOf("1 tablespoon vegetable oil", "1 onion, chopped", "1 sweet potato, peeled", "1 can (14 oz) diced tomatoes" ,"4 cups vegetable broth", "1/2 cup peanut butter"),
                "isPublic" to true,
                "isFavorite" to false
            ),
            mapOf(
                "name" to "Shakshuka",
                "totalMinutes" to 35,
                "totalServings" to 4,
                "cookware" to listOf("Skillet", "Pot"),
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

    //------------Fetch Recipes from Firebase in the Home ActivitY
    private fun fetchRecipesFromDatabase() {
    val userId = auth.currentUser?.uid ?: return
    val databaseReference = FirebaseDatabase.getInstance().getReference("recipes")

    // Fetch the public recipes
    databaseReference.orderByChild("isPublic").equalTo(true)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipesList.clear()
                // Fetch and add all public recipes to the list
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let { recipesList.add(it) }
                }

                // Fetch the current user's recipes (private and public)
                fetchUserRecipes(userId)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }

    private fun fetchUserRecipes(userId: String) {
        val userRecipeReference = FirebaseDatabase.getInstance().getReference("recipes").child(userId)

        userRecipeReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Add user's recipes (both public and private) to the list
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let { recipesList.add(it) }
                }

                // After fetching both public and user-specific recipes, update the adapter
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
        favoritesRecView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)  // Set to horizontal orientation
    }



}