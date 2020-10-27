package com.sergeenko.alexey.noble.dataclasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.sergeenko.alexey.noble.stringToByteArray
import com.sergeenko.alexey.noble.toByteArray

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