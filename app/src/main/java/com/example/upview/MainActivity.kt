package com.example.upview

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupCurrency)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val cardPopular = findViewById<CardView>(R.id.cardPopular)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val currency = if (checkedId == R.id.radioUsd) "USD" else "EUR"
            Toast.makeText(this, "Selected currency: $currency", Toast.LENGTH_SHORT).show()
        }

//        searchEditText.addTextChangedListener {
//            // TODO: фильтрация списка
//        }

        cardPopular.setOnClickListener {
            startActivity(Intent(this, CryptoDetailsActivity::class.java))
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}