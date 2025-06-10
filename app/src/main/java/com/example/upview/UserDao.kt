package com.example.upview.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    // Изменяем возвращаемый тип на LiveData
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun getUser(email: String, password: String): LiveData<User?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Int): LiveData<User?>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Int)
}