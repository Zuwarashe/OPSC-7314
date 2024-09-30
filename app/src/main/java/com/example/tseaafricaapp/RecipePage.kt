package com.example.tseaafricaapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_page)

        lblRecipeName = findViewById(R.id.lblRecipeName)
        lblMinutes = findViewById(R.id.lblMinutes)
        lblServings = findViewById(R.id.lblServings)
        imageRecipe = findViewById(R.id.imageRecipe)
        imageBtnFavourite = findViewById(R.id.imageBtnFavourite)
        btnCookware = findViewById(R.id.btnCookware)
        btnIngredients = findViewById(R.id.btnIngredients)
        btnInstructions = findViewById(R.id.btnInstructions)
        recyclerView = findViewById(R.id.recyclerView)

        val recipeId = intent.getStringExtra("RECIPE_ID")
        if (recipeId != null) {
            fetchRecipeDetails(recipeId)
        }

        //button to go back
        findViewById<ImageButton>(R.id.imageBtnBack).setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    //method that fetches details from the database
    private fun fetchRecipeDetails(recipeId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("recipes").child(userId).child(recipeId)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipe = snapshot.getValue(Recipe::class.java)
                    recipe?.let {
                        //displaying the details
                        lblRecipeName.text = it.name
                        lblMinutes.text = "${it.totalMinutes} minutes"
                        lblServings.text = "${it.totalServings} servings"

                        //when button is clicked it will display the cookware
                        btnCookware.setOnClickListener {
                            btnCookware.setBackgroundColor(Color.parseColor("#FED8B1"))
                            btnIngredients.setBackgroundColor(Color.parseColor("White"))
                            btnInstructions.setBackgroundColor(Color.parseColor("White"))

                            val cookwareList = recipe?.cookware ?: listOf() // Retrieve cookware list from the recipe
                            displayCookwareList(cookwareList)
                        }

                        //when button is clicked it will display the ingredients
                        btnIngredients.setOnClickListener {
                            btnIngredients.setBackgroundColor(Color.parseColor("#FED8B1"))
                            btnCookware.setBackgroundColor(Color.parseColor("White"))
                            btnInstructions.setBackgroundColor(Color.parseColor("White"))

                            val ingredientsList = recipe.ingredients ?: listOf()
                            displayIngredientsList(ingredientsList)
                        }

                        //when button is clicked it will display the instructions
                        btnInstructions.setOnClickListener {
                            btnInstructions.setBackgroundColor(Color.parseColor("#FED8B1"))
                            btnIngredients.setBackgroundColor(Color.parseColor("White"))
                            btnCookware.setBackgroundColor(Color.parseColor("White"))

                            val instructionList = recipe?.instructions ?: listOf() // Retrieve cookware list from the recipe
                            Log.d("RecipePage", "Instructions list: $instructionList")
                            displayInstructionsList(instructionList)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    //--------Display cookware, ingredients and instructions list
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
    //=====END Display lists

    private fun updateFavoriteButton(isFavorite: Boolean) {
        imageBtnFavourite.setImageResource(
            if (isFavorite) R.drawable.favourite_filled
            else R.drawable.favourite_svgrepo_com
        )
    }
}