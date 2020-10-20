package com.sergeenko.alexey.noble.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.sergeenko.alexey.noble.dataclasses.Club

class ClubConverter {

    @TypeConverter
    fun toString(club: Club?): String = Gson().toJson(club)

    @TypeConverter
    fun fromString(club: String): Club? = Gson().fromJson(club, Club::class.java)
}
