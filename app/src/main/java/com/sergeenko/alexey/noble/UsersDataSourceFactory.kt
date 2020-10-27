package com.sergeenko.alexey.noble

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class UsersDataSourceFactory(var trainingList: List<TrainingItem>) : DataSource.Factory<Long, TrainingItem>() {
    private val usersDataSourceLiveData = MutableLiveData<UsersDataSource>()

    override fun create(): DataSource<Long, TrainingItem> {
        val usersDataSource = UsersDataSource(trainingList = trainingList)
        usersDataSourceLiveData.postValue(usersDataSource)
        return usersDataSource
    }

    fun cr() = create()

}