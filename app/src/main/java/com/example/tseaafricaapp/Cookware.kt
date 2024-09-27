package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Cookware : AppCompatActivity() {

    private var cookwareSelected = false
    private var ingredientsSelected = false
    private var instructionsSelected =  false
    private var addBtnClicked =  false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cookware)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
        ///--------------Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.mealPlan

        bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.mealPlan -> true
                R.id.home ->{
                    startActivity(Intent(applicationContext, Home::class.java))
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

        ///--------------Input fragments
        val btnCookware = findViewById<Button>(R.id.btnCookware)
        val btnIngredients = findViewById<Button>(R.id.btnIngredients)
        val btnInstructions = findViewById<Button>(R.id.btnInstructions)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        btnCookware.setOnClickListener{
            cookwareSelected = true
        }

        btnIngredients.setOnClickListener{
            ingredientsSelected = true
        }

        btnInstructions.setOnClickListener{
            instructionsSelected = true
        }

        btnAdd.setOnClickListener{
            if(cookwareSelected == true){
                CookwareFragmentDisplay()
            }
            if(ingredientsSelected == true){
                IngredientsFragmentDisplay()
            }
            if(instructionsSelected == true){
                InstructionsFragmentDisplay()
            }

        }
    }

    private fun CookwareFragmentDisplay(){
        // Create an instance of the fragment
        val cookwareInput = CookwareInput()

        // Begin the fragment transaction
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the existing layout with the fragment
        fragmentTransaction.add(android.R.id.content, cookwareInput)

        // Optionally, add the transaction to the back stack so the user can navigate back
        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }

    private fun IngredientsFragmentDisplay(){
        // Create an instance of the fragment
        val ingredientInput = IngredientInput()

        // Begin the fragment transaction
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the existing layout with the fragment
        fragmentTransaction.add(android.R.id.content, ingredientInput)

        // Optionally, add the transaction to the back stack so the user can navigate back
        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }

    private fun InstructionsFragmentDisplay(){
        // Create an instance of the fragment
        val instructionInput = InstructionInput()

        // Begin the fragment transaction
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the existing layout with the fragment
        fragmentTransaction.add(android.R.id.content, instructionInput)

        // Optionally, add the transaction to the back stack so the user can navigate back
        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }
}