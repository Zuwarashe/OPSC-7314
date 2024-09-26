package com.example.tseaafricaapp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class Cookware : AppCompatActivity() {

    private var cookwareSelected = false
    private var ingredientsSelected = false
    private var instructionsSelected =  false
    private var addBtnClicked =  false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cookware)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val btnCookware = findViewById<Button>(R.id.btnCookware)
        val btnIngredients = findViewById<Button>(R.id.btnIngredients)
        val btnInstructions = findViewById<Button>(R.id.btnInstructions)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        btnCookware.setOnClickListener{
            cookwareSelected = true
        }

        /*btnIngredients.setOnClickListener{
            ingredientsSelected = true
            CheckIngredientsSelected()
        }

        btnInstructions.setOnClickListener{
            instructionsSelected = true
            CheckInstructionsSelected()
        }*/

        btnAdd.setOnClickListener{
            if(cookwareSelected == true){
                CookwareFragmentDisplay()
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
}