package com.sergeenko.alexey.noble.converters

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergeenko.alexey.noble.dataclasses.Measure
import com.sergeenko.alexey.noble.utills.replaceQuoits
import java.lang.reflect.Type
import java.util.*


class MeasureListConvert {

    @TypeConverter
    fun stringToSomeObjectList(d: String?): MutableList<Measure>? {
        val data = d?.let { replaceQuoits(it) } ?: return Collections.emptyList()
        if (data == "" || data == "null" || data == "[]" || data.isEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Measure?>?>() {}.type

        return try {
            Gson().fromJson(data, listType)
        }catch (e:Exception){
            Log.i("AASJKDJNASDJ", data)
            Collections.emptyList()
        }
    }

    @TypeConverter
    fun someObjectListToString(someObjects: MutableList<Measure>?): String? {
        return Gson().toJson(someObjects)
    }
}

