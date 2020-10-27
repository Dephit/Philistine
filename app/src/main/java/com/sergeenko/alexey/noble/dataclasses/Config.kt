package com.sergeenko.alexey.noble.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Config(
    @PrimaryKey(autoGenerate = false)
    val id: String = "nobleConfig",
    var selectedLanguageCode: String = "ru",
    var countryCode: String = "ru"
)