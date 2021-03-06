package com.sergeenko.alexey.noble.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sergeenko.alexey.noble.converters.MuscleListTypeConveter
import com.sergeenko.alexey.noble.daos.TrainingDao
import com.sergeenko.alexey.noble.dataclasses.TrainingItem
import com.sergeenko.alexey.noble.converters.BitmapConvector
import com.sergeenko.alexey.noble.converters.ClubConverter
import com.sergeenko.alexey.noble.converters.MeasureListConvert
import com.sergeenko.alexey.noble.converters.TrainingListConvert
import com.sergeenko.alexey.noble.daos.ClientDao
import com.sergeenko.alexey.noble.daos.ConfigDao
import com.sergeenko.alexey.noble.daos.UserDao
import com.sergeenko.alexey.noble.dataclasses.*

@Database(entities = [User::class, LangList::class, Config::class, Client::class, TrainingItem::class/*, Club::class*/], version = 38)
@TypeConverters(
        LanguageConverter::class,
        ClubConverter::class,
        BitmapConvector::class,
        MeasureConvert::class,
        MeasureListConvert::class,
        TrainingListConvert::class,
        MuscleListTypeConveter::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun languageDao(): LanguageDao
    abstract fun config(): ConfigDao
    abstract fun clientDao(): ClientDao
    abstract fun trainingDao(): TrainingDao
    //abstract fun clubDao(): ClubDao
}