package com.example.upview

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CryptoDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_details)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupPeriod)
        val checkBox = findViewById<CheckBox>(R.id.favoriteCheckBox)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val period = when (checkedId) {
                R.id.radioDay -> "1D"
                R.id.radioWeek -> "1W"
                R.id.radioMonth -> "1M"
                else -> "1Y"
            }
            Toast.makeText(this, "Selected period: $period", Toast.LENGTH_SHORT).show()
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "Added to favorites" else "Removed", Toast.LENGTH_SHORT).show()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}