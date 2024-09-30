package com.example.tseaafricaapp

import android.annotation.SuppressLint
import android.os.Bundle
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

    private fun displayCookwareList(cookwareList: List<String>) {
        val adapter = CookwareAdapter(cookwareList)
        recyclerView.adapter = adapter
    }

    private fun displayInstructionsList(instructionsList: List<String>) {
        val adapter = InstructionsAdapter(instructionsList)
        recyclerView.adapter = adapter
    }


    private fun fetchRecipeDetails(recipeId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("recipes").child(userId).child(recipeId)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipe = snapshot.getValue(Recipe::class.java)
                    recipe?.let {
                        lblRecipeName.text = it.name
                        lblMinutes.text = "${it.totalMinutes} minutes"
                        lblServings.text = "${it.totalServings} servings"

                        btnCookware.setOnClickListener {
                            val cookwareList = recipe?.cookware ?: listOf() // Retrieve cookware list from the recipe
                            displayCookwareList(cookwareList)
                        }

                        btnInstructions.setOnClickListener {
                            val instructionsList = recipe?.instructions ?: listOf() // Retrieve cookware list from the recipe
                            displayInstructionsList(instructionsList)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
}