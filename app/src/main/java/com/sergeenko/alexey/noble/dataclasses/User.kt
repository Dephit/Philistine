package com.sergeenko.alexey.noble.dataclasses

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sergeenko.alexey.noble.converters.ClubConverter
import okhttp3.MultipartBody

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
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

    @Ignore
    fun getMultiPartField(): MutableList<MultipartBody.Part> = MultipartBody.Builder()
            .addFormDataPart("user_email", email)
            .addFormDataPart("user_pass", pass)
            .addFormDataPart("club_id", club?.id.toString())
            .build().parts()
}


