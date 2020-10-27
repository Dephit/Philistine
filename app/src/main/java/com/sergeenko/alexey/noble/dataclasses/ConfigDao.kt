package com.sergeenko.alexey.noble.dataclasses

import androidx.room.*

@Dao
interface ConfigDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfig(user: Config)

    @Update
    fun updateConfig(user: Config)

    @Delete
    fun deleteConfig(gender: Config)

    @Query("SELECT * FROM Config")
    fun getUser(): List<Config>
}