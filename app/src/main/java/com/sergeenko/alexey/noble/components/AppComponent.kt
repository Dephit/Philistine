package com.sergeenko.alexey.noble.components

import android.content.Context
import androidx.room.Database
import com.sergeenko.alexey.noble.*
import com.sergeenko.alexey.noble.database.AppDatabase
import com.sergeenko.alexey.noble.dataclasses.*
import com.sergeenko.alexey.noble.modules.AppModule
import com.sergeenko.alexey.noble.modules.NetModule
import com.sergeenko.alexey.noble.modules.RoomModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class, NetModule::class])
interface AppComponent {

    fun inject(activity: BaseActivity)

    fun database(): AppDatabase
    fun getLanguageDao(): LanguageDao
    fun userDao(): UserDao
    fun getConfigDao(): ConfigDao
    fun clientDao(): ClientDao
    fun trainingDao(): TrainingDao

    fun getUser(): User?
    fun getLocaleLanguage(): Language
    fun getConfig(): Config?
    fun nobleApi(): NobleApi
    fun serviceInterceptor(): ServiceInterceptor

}