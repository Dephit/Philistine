package com.sergeenko.alexey.noble.dataclasses

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergeenko.alexey.noble.TrainingItem
import com.sergeenko.alexey.noble.replaceQuoits
import java.lang.Exception
import java.lang.reflect.Type
import java.util.*

class TrainingListConvert {

    @TypeConverter
    fun stringObjectList(d: String?): MutableList<TrainingItem>? {
        val data = d?.let { replaceQuoits(it) } ?: return Collections.emptyList()
        if (data == "" || data == "null" || data == "[]" || data.isEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<TrainingItem?>?>() {}.type

        return try {
            Gson().fromJson(data, listType)
        }catch (e: Exception){
            Log.i("AASJKDJNASDJ", data)
            Collections.emptyList()
        }
    }

    @TypeConverter
    fun toString(someObjects: MutableList<TrainingItem>?): String? {
        return Gson().toJson(someObjects)
    }
}