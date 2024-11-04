package com.example.tseaafricaapp

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import android.Manifest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
//import com.android.volley.Request
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream

import okhttp3.*
import okhttp3.Request


import java.io.IOException




class Cookware : AppCompatActivity() {
//-------------------------------------------------------ADD PHOTO------------------------------------------------------------------------
    private lateinit var photoImageView: ImageView
    private lateinit var selectPhotoButton: Button
    private lateinit var takePhotoButton: Button
    private lateinit var storage: FirebaseStorage
    private var photoUri: Uri? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Recipe Photo")
            put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        }
        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        photoUri?.let { uri ->
            cameraLauncher.launch(uri)
        }
    }
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let { uri ->
                photoImageView.setImageURI(uri)
            }
        }
    }
    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pickImageFromGallery()
        } else {
            Toast.makeText(this, "Storage permission is required to select photos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImageFromGallery() {
        galleryLauncher.launch("image/*")
    }
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            photoUri = it
            photoImageView.setImageURI(it)
        }
    }
//======================================================END: ADD PHOTO======================================================



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

//=======END :Claude  METHOD save into realtime database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cookware)
//------------------------------------------Photo add
    // Initialize Firebase Storage
    storage = FirebaseStorage.getInstance()

    // Initialize views
    photoImageView = findViewById(R.id.photoImageView)
    selectPhotoButton = findViewById(R.id.selectPhotoButton)
    takePhotoButton = findViewById(R.id.takePhotoButton)

    selectPhotoButton.setOnClickListener {
        checkGalleryPermission()
    }

    takePhotoButton.setOnClickListener {
        checkCameraPermission()
    }

//==========================================END:Photo add


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

        findViewById<ImageButton>(R.id.imageBtnBack).setOnClickListener {
            startActivity(Intent(applicationContext, Home::class.java))
        }

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            saveRecipe()
        }

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
    }
//------------------------------------------Photo ------------------------------------------
    private fun checkGalleryPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickImageFromGallery()
            }
            else -> {
                galleryPermissionLauncher.launch(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

//=============================================END: Photo    =============================================

    //Method adding instructions to list
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

    //Method adding ingredients to list
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

    //Method adding cookware to list
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
    private fun logRecipeEvent(newRecipeCount: Int) {
        val bundle = Bundle().apply {
            putInt("recipe_count", newRecipeCount) // Log the new recipe count
        }
        FirebaseAnalytics.getInstance(this).logEvent("recipe_saved", bundle) // Custom event name
    }


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

        // Check if there is a selected photo to upload
        if (photoUri != null) {
            // Upload the image to Firebase Storage
            val storageRef = storage.reference.child("recipe_images/${recipeId}.jpg")
            storageRef.putFile(photoUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL of the uploaded image
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // Save the recipe data with the image URL
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
                            "imageUrl" to downloadUrl.toString() // Save the image URL
                        )

                        database.child("recipes").child(userId).child(recipeId).setValue(recipe)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show()
                                if (isPublic) {
                                    FirebaseMessaging.getInstance().subscribeToTopic("recipes")
                                    sendRecipeNotification()
                                }
                                clearInputs()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        } else {
            // If no photo is selected, save the recipe without an image
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
                "imageUrl" to "" // Set an empty URL if no image is uploaded
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

    private fun sendRecipeNotification() {
        val title = "New Recipe Added" // Define the title for the notification
        val message = "A new recipe has been added to the collection!" // Define the message for the notification

        val serviceAccountKeyPath = "C:/Users/neoma/StudioProjects/TseaAfricaApp/backend/config/serviceAccountKey.json"
        val credentials = GoogleCredentials.fromStream(FileInputStream(serviceAccountKeyPath))
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        credentials.refreshIfExpired()
        val accessToken = credentials.accessToken.tokenValue

        // FCM API URL
        val fcmUrl = "https://fcm.googleapis.com/v1/projects/tseaafricadb-532c4/messages:send"

        // JSON Payload
        val jsonPayload = JSONObject().apply {
            put("message", JSONObject().apply {
                put("topic", "recipes")
                put("notification", JSONObject().apply {
                    put("title", title)
                    put("body", message)
                })
            })
        }

        val client = OkHttpClient()
        val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(fcmUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to send notification", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("FCM", "Notification sent successfully")
                } else {
                    Log.e("FCM", "Error sending notification: ${response.body?.string()}")
                }
            }
        })
    }


    private fun clearInputs(){
        txtName.text.clear()
        txtMinutes.text.clear()
        txtServings.text.clear()
        chkPublic.isChecked = false
    }

}
