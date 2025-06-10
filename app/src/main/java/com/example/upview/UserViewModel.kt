package com.example.upview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upview.data.User
import com.example.upview.data.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    // Теперь эти методы возвращают LiveData напрямую
    fun getUser(email: String, password: String): LiveData<User?> {
        return repository.getUser(email, password)
    }

    fun getUserById(userId: Int): LiveData<User?> {
        return repository.getUserById(userId)
    }

    fun deleteUser(userId: Int) = viewModelScope.launch {
        repository.deleteUser(userId)
    }
}