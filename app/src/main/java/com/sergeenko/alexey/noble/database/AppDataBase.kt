package com.sergeenko.alexey.noble.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sergeenko.alexey.noble.converters.ClubConverter
import com.sergeenko.alexey.noble.dataclasses.*

@Database(entities = [User::class, LangList::class, Config::class, Client::class/*, Club::class*/], version = 32)
@TypeConverters(
        LanguageConverter::class,
        ClubConverter::class,
        BitmapConvector::class,
        MeasureConvert::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun languageDao(): LanguageDao
    abstract fun config(): ConfigDao
    abstract fun clientDao(): ClientDao
    //abstract fun clubDao(): ClubDao
}