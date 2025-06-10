package com.example.upview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.upview.data.AppDatabase
import com.example.upview.data.User
import com.example.upview.data.UserRepository
import com.example.upview.databinding.ActivityRegisterBinding
import com.example.upview.viewmodel.UserViewModel
import com.example.upview.viewmodel.UserViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(database.userDao())
        userViewModel = ViewModelProvider(this, UserViewModelFactory(repository)).get(UserViewModel::class.java)

        binding.registerButton.setOnClickListener {
            val nickname = binding.nicknameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (validateInput(nickname, email, password, confirmPassword)) {
                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val creationDate = sdf.format(Date())

                val user = User(
                    nickname = nickname,
                    email = email,
                    password = password, // В реальном приложении хешируйте пароль!
                    creationDate = creationDate
                )

                userViewModel.insertUser(user)
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun validateInput(
        nickname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (nickname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}