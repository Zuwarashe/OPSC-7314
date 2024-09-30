package com.example.tseaafricaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Favourites : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)  // Call setContentView first to inflate the layout

        // Find the main layout view by ID
        val mainView = findViewById<View>(R.id.main)

        // Apply window insets only if the view is not null
        mainView?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                // Get system bar insets (status bar, navigation bar, etc.)
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                // Apply padding to the view to accommodate system bars
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        ///--------------Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.fave

        bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.fave -> true
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
}
