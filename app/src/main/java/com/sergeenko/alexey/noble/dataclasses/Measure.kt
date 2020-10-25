package com.sergeenko.alexey.noble.dataclasses

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import java.io.Serializable
import java.util.*

data class Measure(
        var id: Int? = null,
        var dateOfMeasure: Long? = null,
        var measures: Measures = Measures(),
        var params: Params = Params()
): Serializable{
    fun isEmpty() = measures.isEmpty() && params.isEmpty()

    fun isNotEmpty() = !isEmpty()
}

class MeasureConvert{

    @TypeConverter
    fun toJson(measure: Measure?): String? = Gson().toJson(measure)

    @TypeConverter
    fun fromJson(str: String?): Measure? = Gson().fromJson(str, Measure::class.java)
}