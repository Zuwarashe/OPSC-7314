package com.example.tseaafricaapp


import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class Settings : AppCompatActivity() {
    private lateinit var spinner: Spinner
    companion object {
        val languages = arrayOf("Select Language", "English", "Afrikaans")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        spinner = findViewById(R.id.spinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLang = parent.getItemAtPosition(position).toString()

                if (selectedLang == "English") {
                    setLocal(this@Settings, "eng")
                    finish()
                    startActivity(intent)
                }
                else if(selectedLang == "Afrikaans"){
                    setLocal(this@Settings, "afr")
                    finish()
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when no item is selected, if needed
            }
        }


        // Get the root view of each included layout and set click listeners
        val healthGoalLayout = findViewById<LinearLayout>(R.id.health_goal_btn)
        val yourRecipesLayout = findViewById<LinearLayout>(R.id.your_recipes_btn)
        val eatinPrefLayout = findViewById<LinearLayout>(R.id.eatin_pref_btn)
        val meetOurTeamLayout = findViewById<LinearLayout>(R.id.meet_our_team_btn)

        healthGoalLayout.setOnClickListener {
            val intent = Intent(this, UserPrefence::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        yourRecipesLayout.setOnClickListener {
            val intent = Intent(this, Favourites::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        eatinPrefLayout.setOnClickListener {
            val intent = Intent(this, UserPrefence::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }

        meetOurTeamLayout.setOnClickListener {
            val intent = Intent(this, Team::class.java)  // Make sure to replace with your actual signup activity class
            startActivity(intent)
        }
        ///--------------Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.settings

        bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.settings -> true
                R.id.home ->{
                    startActivity(Intent(applicationContext, Home::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
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
                else -> false
            }
        }
///--------------Navigation end
    }

    fun setLocal(activity: Activity, langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
}