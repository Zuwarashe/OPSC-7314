package com.example.tseaafricaapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


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

//------------friebase storage
private lateinit var storage: FirebaseStorage
    private lateinit var imgRecipe: ImageView
    private lateinit var btnSelectImage: Button
    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imgRecipe.setImageURI(selectedImageUri)
        }
    }



//============END: firebase storage



//=======END :Claude  METHOD save into realtime databas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cookware)
//--------firebase storage
        storage = FirebaseStorage.getInstance()
        imgRecipe = findViewById(R.id.imgRecipe)
        btnSelectImage = findViewById(R.id.btnSelectImage)

        btnSelectImage.setOnClickListener {
            openImageChooser()
        }


//========END: firebase storage
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
//-------------firebase storage

//============END:firebase storage

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




//--------------Firebase storage
    private fun openImageChooser() {
        getContent.launch("image/*")
    }
    private fun uploadImage(callback: (String?) -> Unit) {
        if (selectedImageUri == null) {
            callback(null)
            return
        }
        val storageRef = storage.reference
        val imageRef = storageRef.child("recipe_images/${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }




//==============END: Firebase storage


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

    private fun saveRecipe() {
        uploadImage { imageUrl ->
            val userId = auth.currentUser?.uid ?: return@uploadImage
            val recipeId = database.child("recipes").push().key ?: return@uploadImage

            val recipeName = txtName.text.toString().trim()
            val totalMinutes = txtMinutes.text.toString().toIntOrNull() ?: 0
            val totalServings = txtServings.text.toString().toIntOrNull() ?: 0
            val isPublic = chkPublic.isChecked

            if (recipeName.isEmpty()) {
                Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show()
                return@uploadImage
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
                "isFavorite" to false,
                "imageUrl" to imageUrl
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
    }



    private fun clearInputs(){
        txtName.text.clear()
        txtMinutes.text.clear()
        txtServings.text.clear()
        chkPublic.isChecked = false

    }
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
