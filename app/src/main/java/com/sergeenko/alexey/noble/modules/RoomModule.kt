package com.sergeenko.alexey.noble.modules

import android.app.Application
import androidx.room.Room
import com.sergeenko.alexey.noble.TrainingDao
import com.sergeenko.alexey.noble.database.AppDatabase
import com.sergeenko.alexey.noble.dataclasses.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: Application?) {
    private val demoDatabase: AppDatabase = Room.databaseBuilder(mApplication!!, AppDatabase::class.java, "noble-db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        return demoDatabase
    }

    @Singleton
    @Provides
    fun providesUserDao(demoDatabase: AppDatabase): UserDao {
        return demoDatabase.userDao()
    }

    @Singleton
    @Provides
    fun providesClientDao(demoDatabase: AppDatabase): ClientDao {
        return demoDatabase.clientDao()
    }

    @Provides
    fun providesUser(userDao: UserDao): User? {
        return userDao.getUser().firstOrNull()
    }

    @Singleton
    @Provides
    fun providesConfigDao(demoDatabase: AppDatabase): ConfigDao {
        return demoDatabase.config()
    }

    @Singleton
    @Provides
    fun providesTrainingDao(demoDatabase: AppDatabase): TrainingDao = demoDatabase.trainingDao()

    @Provides
    fun providesConfig(demoDatabase: AppDatabase): Config = demoDatabase.config().getUser().firstOrNull() ?: Config()

    @Singleton
    @Provides
    fun providesLanguageDao(demoDatabase: AppDatabase): LanguageDao = demoDatabase.languageDao()


    @Singleton
    @Provides
    fun providesLanguage(languageDao: LanguageDao, config: Config?): Language {
        return config?.selectedLanguageCode?.let {
            languageDao.getLanguage(it).firstOrNull()?.body ?: Language()
        } ?: Language()
    }

}

