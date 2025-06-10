package com.example.upview

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.upview.data.AppDatabase
import com.example.upview.data.UserRepository
import com.example.upview.databinding.ActivityLoginBinding
import com.example.upview.viewmodel.UserViewModel
import com.example.upview.viewmodel.UserViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(database.userDao())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.getUser(email, password).observe(this) { user ->
                    if (user != null) {
                        with(sharedPreferences.edit()) {
                            putInt("current_user_id", user.id)
                            apply()
                        }
                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}