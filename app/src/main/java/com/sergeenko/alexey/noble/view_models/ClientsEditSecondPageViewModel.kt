package com.sergeenko.alexey.noble.view_models

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sergeenko.alexey.noble.dataclasses.TrainingItem
import com.sergeenko.alexey.noble.factories.UsersDataSourceFactory
import com.sergeenko.alexey.noble.dataclasses.Client

class ClientsEditSecondPageViewModel(application: Application,val client: Client) : BaseViewModel(application) {
    var userList: LiveData<PagedList<TrainingItem>>? = null
    private val sourceFactory: UsersDataSourceFactory = UsersDataSourceFactory(client.trainingList!!.reversed())

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10 * 2)
                .setEnablePlaceholders(false)
                .build()
        userList = LivePagedListBuilder(sourceFactory, config).build()
    }
}

