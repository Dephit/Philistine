package com.sergeenko.alexey.noble

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergeenko.alexey.noble.dataclasses.Client

class ModelFactory(val application: Application, private val client: Client) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == ClientEditViewModel::class.java) {
            ClientEditViewModel(application = application, client = client) as T
        } else BaseViewModel(application) as T
    }
}