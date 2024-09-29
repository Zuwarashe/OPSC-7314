package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Cookware : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var txtName: EditText
    private lateinit var txtMinutes: EditText
    private lateinit var txtServings: EditText
    private lateinit var chkPublic: CheckBox

    private lateinit var txtCookware: EditText
    private lateinit var btnAddCookware: Button
    private lateinit var cookwareList: MutableList<String>

    private lateinit var txtIngredient: EditText
    private lateinit var txtQuantity: EditText
    private lateinit var txtMeasurement: EditText

    private lateinit var btnAddIngredient: Button
    private lateinit var ingredientsList: MutableList<String>

    private lateinit var txtInstruction: EditText
    private lateinit var btnAddInstruction: Button
    private lateinit var instructionList: MutableList<String>



//=======END :Claude  METHOD save into realtime databas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cookware)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        txtName = findViewById(R.id.txtName)
        txtMinutes = findViewById(R.id.txtMinutes)
        txtServings = findViewById(R.id.txtServings)
        chkPublic = findViewById(R.id.chkPublic)

        txtCookware = findViewById(R.id.txtCookware)
        btnAddCookware = findViewById(R.id.btnAddCookware)
        chkPublic = findViewById(R.id.chkPublic)

        txtIngredient = findViewById(R.id.txtIngredient)
        txtQuantity = findViewById(R.id.txtQuantity)
        txtMeasurement = findViewById(R.id.txtMeasurement)

        btnAddIngredient = findViewById(R.id.btnAddIngredient)

        txtInstruction = findViewById(R.id.txtInstruction)
        btnAddInstruction = findViewById(R.id.btnAddInstruction)



        cookwareList = mutableListOf()

        ingredientsList = mutableListOf()

        instructionList = mutableListOf()

        btnAddCookware.setOnClickListener {
        addCookwareToList()
        }

        btnAddIngredient.setOnClickListener {
        addIngredientToList()
        }

        btnAddInstruction.setOnClickListener {
        addInstructionToList()
        }
        val btnSave = findViewById<Button>(R.id.btnSave)


        btnSave.setOnClickListener {
            saveRecipe()
        }

//-----Fragment
//------END: Fragment
///------------------------Navigation
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
    }

    private fun addInstructionToList() {
        val instructionItem = txtInstruction.text.toString()
        if (instructionItem.isNotEmpty()) {
            instructionList.add(instructionItem)
            Toast.makeText(this, "$instructionItem added to instructionItem list", Toast.LENGTH_SHORT).show()
            txtInstruction.text.clear() // Clear the input field after adding
        } else {
            Toast.makeText(this, "Please enter instructionItem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addIngredientToList() {
        val ingredientItem  = txtIngredient.text.toString()
        val quantityItem  = txtQuantity.text.toString().toDoubleOrNull() ?: 0.0
        val measurementItem  = txtMeasurement.text.toString()

        if (ingredientItem.isNotEmpty() && measurementItem.isNotEmpty()) {
            // Add ingredient details as a formatted string or a map
            val ingredientEntry = "$quantityItem $measurementItem of $ingredientItem"
            ingredientsList.add(ingredientEntry)

            // Clear inputs after adding
            txtIngredient.text.clear()
            txtQuantity.text.clear()
            txtMeasurement.text.clear()

            Toast.makeText(this, "Ingredient added: $ingredientEntry", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
        }


    }

    private fun addCookwareToList() {
        val cookwareItem = txtCookware.text.toString()
        if (cookwareItem.isNotEmpty()) {
            cookwareList.add(cookwareItem)
            Toast.makeText(this, "$cookwareItem added to cookware list", Toast.LENGTH_SHORT).show()
            txtCookware.text.clear() // Clear the input field after adding
        } else {
            Toast.makeText(this, "Please enter cookware", Toast.LENGTH_SHORT).show()
        }
    }

    //---------Claude  METHOD save into realtime database
    private fun saveRecipe() {
        val userId = auth.currentUser?.uid ?: return
        val recipeId = database.child("recipes").push().key ?: return

        val recipeName = txtName.text.toString().trim()
        val totalMinutes = txtMinutes.text.toString().toIntOrNull() ?: 0
        val totalServings = txtServings.text.toString().toIntOrNull() ?: 0
        val isPublic = chkPublic.isChecked

        if (recipeName.isEmpty()) {
            Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = hashMapOf(
            "recipeId" to recipeId,
            "userId" to userId,
            "name" to recipeName,
            "totalMinutes" to totalMinutes,
            "totalServings" to totalServings,
            "isPublic" to isPublic,
            "cookware" to cookwareList,
            "instruction" to instructionList,
            "ingredients" to ingredientsList,
            "isFavorite" to false

        )
        database.child("recipes").child(userId).child(recipeId).setValue(recipe)
        .addOnSuccessListener {
            Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show()
            clearInputs()
        }
        .addOnFailureListener {
            Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show()
        }

    }

    private fun clearInputs(){
        txtName.text.clear()
        txtMinutes.text.clear()
        txtServings.text.clear()
        chkPublic.isChecked = false

    }
//======END: Claude  METHOD save into realtime database

}
