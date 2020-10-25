package com.sergeenko.alexey.noble.dataclasses

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergeenko.alexey.noble.replaceQuoits
import retrofit.converter.Converter
import retrofit.mime.TypedInput
import retrofit.mime.TypedOutput
import java.lang.Exception
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

