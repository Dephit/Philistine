package com.sergeenko.alexey.noble.daos

import androidx.room.*
import com.sergeenko.alexey.noble.dataclasses.TrainingItem

@Dao
interface TrainingDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTraningItem(user: TrainingItem)

    @Update
    fun updateTraningItem(user: TrainingItem)

    @Delete
    fun deleteTraningItem(gender: TrainingItem)

    @Query("SELECT * FROM TrainingItem")
    fun getTraningItems(): List<TrainingItem>
}