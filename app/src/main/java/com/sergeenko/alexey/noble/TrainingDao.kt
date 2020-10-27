package com.sergeenko.alexey.noble

import androidx.room.*

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