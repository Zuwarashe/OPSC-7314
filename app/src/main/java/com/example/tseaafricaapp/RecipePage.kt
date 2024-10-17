package com.example.tseaafricaapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class RecipePage : AppCompatActivity() {

    private lateinit var lblRecipeName: TextView
    private lateinit var lblMinutes: TextView
    private lateinit var lblServings: TextView
    private lateinit var imageRecipe: ImageView
    private lateinit var imageBtnFavourite: ImageButton
    private lateinit var btnCookware: Button
    private lateinit var btnIngredients: Button
    private lateinit var btnInstructions: Button
    private lateinit var recyclerView: RecyclerView

    //-------FAV
    private var isFavorite: Boolean = false

    // Array of drawable resource IDs
    private val drawables = intArrayOf(
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
        R.drawable.image6,
        // Add more drawable IDs as needed
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_page)

        // Initialize views
        val favoriteCheckBox = findViewById<CheckBox>(R.id.checkBox2)

        // Set listener for the checkbox
        favoriteCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Show "recipe added to favourites" message when checked
                Toast.makeText(this, "Recipe added to favourites", Toast.LENGTH_SHORT).show()
            } else {
                // Show "item removed from favourites" message when unchecked
                Toast.makeText(this, "Item removed from favourites", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize views
        lblRecipeName = findViewById(R.id.lblRecipeName)
        lblMinutes = findViewById(R.id.lblMinutes)
        lblServings = findViewById(R.id.lblServings)
        imageRecipe = findViewById(R.id.imageRecipe)
        //imageBtnFavourite = findViewById(R.id.imageBtnFavourite)
        btnCookware = findViewById(R.id.btnCookware)
        btnIngredients = findViewById(R.id.btnIngredients)
        btnInstructions = findViewById(R.id.btnInstructions)
        recyclerView = findViewById(R.id.recyclerView)

        // Set a random image
        setRandomImage()

        val recipeId = intent.getStringExtra("RECIPE_ID")
        if (recipeId != null) {
            fetchRecipeDetails(recipeId)
        }

        // Handle the back button
        findViewById<ImageButton>(R.id.imageBtnBack).setOnClickListener {
            finish()
        }

        // Adjust window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        /////-----favorite button logic
        imageBtnFavourite.setOnClickListener {
            recipeId?.let { id ->
                isFavorite = !isFavorite // Toggle favorite state
                updateFavoriteButton(isFavorite) // Update button state
                updateFavoriteStatusInDatabase(id, isFavorite) // Save to database
            } ?: run {
                Log.e("RecipePage", "Error: recipeId is null")
            }
        }
    }

    // Function to update the image based on favorite status
    private fun updateFavoriteButton(isFavorite: Boolean) {
        imageBtnFavourite.apply {
            setBackgroundColor(Color.TRANSPARENT) // Ensure background is transparent
            setImageResource(
                if (isFavorite) R.drawable.favourite_filled // Red heart when favorite
                else R.drawable.favourite_svgrepo_com // Gray heart when not favorite
            )
        }
    }

    private fun updateFavoriteStatusInDatabase(recipeId: String, isFavorite: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("recipes").child(userId).child(recipeId)
            databaseReference.child("isFavorite").setValue(isFavorite)
        }
    }

    private fun setRandomImage() {
        val randomIndex = Random.nextInt(drawables.size)
        imageRecipe.setImageResource(drawables[randomIndex])
    }

    private fun fetchRecipeDetails(recipeId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("recipes").child(userId).child(recipeId)

            // Retrieve recipe details
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipe = snapshot.getValue(Recipe::class.java)
                    recipe?.let {
                        lblRecipeName.text = it.name
                        lblMinutes.text = "${it.totalMinutes} minutes"
                        lblServings.text = "${it.totalServings} servings"

                        // Load favorite status from the database
                        val isFavorite = snapshot.child("isFavorite").getValue(Boolean::class.java) ?: false
                        updateFavoriteButton(isFavorite)

                        // Other UI updates here...
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    // Display cookware, ingredients, and instructions list
    private fun displayCookwareList(cookwareList: List<String>) {
        val adapter = CookwareAdapter(cookwareList)
        recyclerView.adapter = adapter
    }

    private fun displayIngredientsList(ingredientsList: List<String>) {
        val adapter = IngredientsAdapter(ingredientsList)
        recyclerView.adapter = adapter
    }

    private fun displayInstructionsList(instructionList: List<String>) {
        val adapter = InstructionsAdapter(instructionList)
        recyclerView.adapter = adapter
    }
}
