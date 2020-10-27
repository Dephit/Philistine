package com.sergeenko.alexey.noble

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource

class UsersDataSource(var trainingList: List<TrainingItem>) : ItemKeyedDataSource<Long, TrainingItem>() {

    private var key = 0

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<TrainingItem>) {
        getProducts(callback)
    }

    private fun getProducts(callback: LoadCallback<TrainingItem>)  {
        var min = key
        min = if(min >= trainingList.size)
            trainingList.size - 1
        else min
        var max = key + 20
        max = if(max >= trainingList.size)
            trainingList.size - 1
        else max
        callback.onResult(trainingList.subList(min, max))
    }


    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<TrainingItem>) {
        key +=20
        getProducts(callback)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<TrainingItem>) {}

    override fun getKey(item: TrainingItem): Long {
        return item.id.toLong()
    }


}