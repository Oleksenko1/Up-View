package com.example.upview

import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.upview.data.AppDatabase
import com.example.upview.data.UserRepository
import com.example.upview.viewmodel.UserViewModel
import com.example.upview.viewmodel.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(database.userDao())
        UserViewModelFactory(repository)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences  = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences .getInt("current_user_id", -1)

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            logoutUser()
        }

        if (userId != -1) {
            userViewModel.getUserById(userId).observe(this, Observer { user ->
                user?.let {
                    findViewById<TextView>(R.id.nicknameTextView).text = "Nickname: ${it.nickname}"
                    findViewById<TextView>(R.id.emailTextView).text = "Email: ${it.email}"
                    findViewById<TextView>(R.id.creationDateTextView).text = "Joined: ${it.creationDate}"
                }
            })
        } else {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }


    }
    private fun logoutUser() {
        with(sharedPreferences.edit()) {
            remove("current_user_id")
            apply()
        }

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()

        Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show()
    }


}