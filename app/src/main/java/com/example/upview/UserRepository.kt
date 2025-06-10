package com.example.upview.data

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    fun getUser(email: String, password: String) = userDao.getUser(email, password)
    fun getUserById(userId: Int) = userDao.getUserById(userId)
    suspend fun deleteUser(userId: Int) = userDao.deleteUser(userId)
}