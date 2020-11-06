package com.sergeenko.alexey.noble.modules

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sergeenko.alexey.noble.NobleApi
import com.sergeenko.alexey.noble.ServiceInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule() {

    private var mBaseUrl: String = "http://noble.gensol.ru/"

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideServiceInterceptor(): ServiceInterceptor {
        return ServiceInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache?, serviceInterceptor: ServiceInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.cache(cache)
        clientBuilder.readTimeout(60, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.addInterceptor(NoConnectionInterceptor())
        //clientBuilder.addInterceptor(serviceInterceptor)

        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        clientBuilder.addInterceptor(logging)
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideNobleApi(retrofit: Retrofit): NobleApi {
        return retrofit.create(NobleApi::class.java)
    }
}
