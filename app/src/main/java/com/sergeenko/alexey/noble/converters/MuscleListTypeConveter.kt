package com.sergeenko.alexey.noble.converters

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergeenko.alexey.noble.dataclasses.Muscle
import com.sergeenko.alexey.noble.utills.replaceQuoits
import java.lang.reflect.Type
import java.util.*

class MuscleListTypeConveter {

    @TypeConverter
    fun stringObjectList(d: String?): MutableList<Muscle>? {
        val data = d?.let { replaceQuoits(it) } ?: return Collections.emptyList()
        if (data == "" || data == "null" || data == "[]" || data.isEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Muscle?>?>() {}.type

        return try {
            Gson().fromJson(data, listType)
        }catch (e: Exception){
            Log.i("AASJKDJNASDJ", data)
            Collections.emptyList()
        }
    }

    @TypeConverter
    fun toString(someObjects: MutableList<Muscle>?): String? {
        return Gson().toJson(someObjects)
    }
}