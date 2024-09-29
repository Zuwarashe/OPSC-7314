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
//---Claude  METHOD save into realtime database

    private lateinit var txtName: EditText
    private lateinit var txtMinutes: EditText
    private lateinit var txtServings: EditText
    private lateinit var chkPublic: CheckBox

    private var cookwareList = mutableListOf<String>()
    private val ingredientsList = mutableListOf<IngredientInput.Ingredient>()
    private val instructionsList = mutableListOf<String>()


//=======END :Claude  METHOD save into realtime databas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cookware)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
//------Claude  METHOD save into realtime database
        txtName = findViewById(R.id.txtName)
        txtMinutes = findViewById(R.id.txtMinutes)
        txtServings = findViewById(R.id.txtServings)
        chkPublic = findViewById(R.id.chkPublic)

//=======END: Claude  METHOD save into realtime database
//-------CHAT METHOD save into realtime database
        /*

        val txtName = findViewById<EditText>(R.id.txtName)
        val txtMinutes = findViewById<EditText>(R.id.txtMinutes)
        val txtServings = findViewById<EditText>(R.id.txtServings)
        val chkPublic = findViewById<CheckBox>(R.id.chkPublic)
        */
//-----END CHAT METHOD : save into realtime database
        val btnSave = findViewById<Button>(R.id.btnSave)


        btnSave.setOnClickListener {
            saveRecipe()
//------CHAT METHOD : save into realtime database
            /*
            val name = txtName.text.toString().trim()
            val totalMinutes = txtMinutes.text.toString().trim().toIntOrNull() ?: 0
            val totalServings = txtServings.text.toString().trim().toIntOrNull() ?: 0
            val isPublic = chkPublic.isChecked

            val recipeId = database.child("recipes").push().key ?: return@setOnClickListener

            // Get the list of cookware from the CookwareInput fragment
            val cookwareItems = (supportFragmentManager.findFragmentById(android.R.id.content) as? CookwareInput)?.cookwareList ?: emptyList()

            val recipeData = mapOf(
                "id" to recipeId,
                "name" to name,
                "totalMinutes" to totalMinutes,
                "totalServings" to totalServings,
                "cookware" to cookwareItems,
                "userID" to auth.currentUser?.uid,
                "isPublic" to isPublic,
                "isFavorite" to false // Default value for isFavorite
            )
            database.child("recipes").child(recipeId).setValue(recipeData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show()
                    // Clear inputs after saving
                    txtName.text.clear()
                    txtMinutes.text.clear()
                    txtServings.text.clear()
                    chkPublic.isChecked = false
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save recipe: ${it.message}", Toast.LENGTH_SHORT).show()
                }
*/
//------END CHAT METHOD : save into realtime database
        }

//-----Fragment
        val btnCookware = findViewById<Button>(R.id.btnCookware)
        val btnIngredients = findViewById<Button>(R.id.btnIngredients)
        val btnInstructions = findViewById<Button>(R.id.btnInstructions)

        btnCookware.setOnClickListener {
            CookwareFragmentDisplay()
        }
        btnIngredients.setOnClickListener {
            IngredientsFragmentDisplay()
        }
        btnInstructions.setOnClickListener {
            InstructionsFragmentDisplay()
        }
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
            "ingredients" to ingredientsList.map { ingredient ->
            hashMapOf(
                "name" to ingredient.name,
                "quantity" to ingredient.quantity,
                "measurement" to ingredient.measurement
            )
            },
            "instructions" to instructionsList
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
        cookwareList.clear()
        ingredientsList.clear()
        instructionsList.clear()

    }
//======END: Claude  METHOD save into realtime database
///-------------    Fragment

    private fun CookwareFragmentDisplay(){
        val cookwareInput = CookwareInput()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, cookwareInput)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun IngredientsFragmentDisplay(){
        val ingredientInput = IngredientInput()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, ingredientInput)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun InstructionsFragmentDisplay(){
        val instructionInput = InstructionInput()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, instructionInput)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


//---END: Fragment
}
