package com.sergeenko.alexey.noble.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.sergeenko.alexey.noble.utills.stringToByteArray
import com.sergeenko.alexey.noble.utills.toByteArray

class BitmapConvector {
    @TypeConverter
    fun fromString(value: String?): Bitmap? {
        val ba = value?.stringToByteArray()
        return  try {
            ba?.size?.let { BitmapFactory.decodeByteArray(ba, 0, it) }
        } catch (e: Exception){
            null
        }
    }

    @TypeConverter
    fun toString(bitmap: Bitmap?): String? {
        return bitmap?.toByteArray().contentToString()
    }
}