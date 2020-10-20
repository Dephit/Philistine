package com.sergeenko.alexey.noble.dataclasses

import androidx.room.*
import com.google.gson.Gson

@Entity(tableName = "Languages")
data class LangList(
        @PrimaryKey(autoGenerate = false)
        val name: String = "ru",
        @TypeConverters(LanguageConverter::class)
        val body: Language = Language()
)

class LanguageConverter {

        @TypeConverter
        fun toString(lang: Language): String = Gson().toJson(lang)

        @TypeConverter
        fun fromString(value:String): Language = Gson().fromJson(value, Language::class.java)
}

@Dao
interface LanguageDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertLanguage(user: LangList)

        @Update
        fun updateLanguage(user: LangList)

        @Delete
        fun deleteLanguage(gender: LangList)

        @Query("SELECT * FROM Languages")
        fun getLanguage(): List<LangList>

        @Query("SELECT * FROM Languages WHERE name Like :name")
        fun getLanguage(name: String): List<LangList>
}