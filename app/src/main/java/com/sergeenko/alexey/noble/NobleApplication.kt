package com.sergeenko.alexey.noble

import android.app.Application
import com.sergeenko.alexey.noble.components.AppComponent
import com.sergeenko.alexey.noble.components.DaggerAppComponent
import com.sergeenko.alexey.noble.modules.AppModule
import com.sergeenko.alexey.noble.modules.NetModule
import com.sergeenko.alexey.noble.modules.RoomModule

class NobleApplication: Application() {

    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .netModule(NetModule())
                .build();


    }

}