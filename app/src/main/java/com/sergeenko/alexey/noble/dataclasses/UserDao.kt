package com.sergeenko.alexey.noble.dataclasses

import androidx.room.*

@Dao
interface UserDao{

    @Query("DELETE FROM User")
    fun deleteTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(gender: User)

    @Query("SELECT * FROM User WHERE id LIKE 0")
    fun getUser(): List<User>
}