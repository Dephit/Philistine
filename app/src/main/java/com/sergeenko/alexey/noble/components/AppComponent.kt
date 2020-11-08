package com.sergeenko.alexey.noble.components

import com.sergeenko.alexey.noble.activities.BaseActivity
import com.sergeenko.alexey.noble.apis.NobleApi
import com.sergeenko.alexey.noble.daos.ClientDao
import com.sergeenko.alexey.noble.daos.ConfigDao
import com.sergeenko.alexey.noble.daos.TrainingDao
import com.sergeenko.alexey.noble.daos.UserDao
import com.sergeenko.alexey.noble.database.AppDatabase
import com.sergeenko.alexey.noble.dataclasses.*
import com.sergeenko.alexey.noble.modules.AppModule
import com.sergeenko.alexey.noble.modules.NetModule
import com.sergeenko.alexey.noble.modules.RoomModule
import com.sergeenko.alexey.noble.utills.ServiceInterceptor
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