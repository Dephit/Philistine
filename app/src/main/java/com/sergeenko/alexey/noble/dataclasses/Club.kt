package com.sergeenko.alexey.noble.dataclasses

import androidx.room.*

data class Club(
    val id: String,
    val address: String,
    val clients: String,
    val devices: String,
    val foto: String,
    val page_name: String,
    val parent_id: String,
    val pass_change: String,
    val pass_time: String,
    val pos: String,
    val price: String,
    val tel: String,
    val title: String,
    val trainings: String,
    val ur_face: String,
    val url: String,
    val vis: String
)

/*
@Dao
interface ClubDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClub(user: Club)

    @Update
    fun updateClub(user: Club)

    @Delete
    fun deleteClub(gender: Club)

    @Query("SELECT * FROM Club")
    fun getClubs(): List<Club>
}*/
