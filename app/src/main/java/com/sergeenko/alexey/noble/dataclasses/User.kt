package com.sergeenko.alexey.noble.dataclasses

import androidx.room.*
import com.sergeenko.alexey.noble.converters.ClubConverter

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    var email: String,
    val lastTimeSync: Long,
    val pass: String,
    @TypeConverters(ClubConverter::class)
    val club: Club?
) {
    @Ignore
    fun getFieldMap(): Map<String, String> = mapOf(
            "user_email" to email,
            "user_pass" to pass,
            "club_id" to club?.id.toString()
    )
}

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(gender: User)

    @Query("SELECT * FROM User")
    fun getUser(): List<User>
}


@Entity
data class Config(
    @PrimaryKey(autoGenerate = false)
    val id: String = "nobleConfig",
    var selectedLanguageCode: String = "ru"
)

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