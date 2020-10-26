package com.sergeenko.alexey.noble

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergeenko.alexey.noble.dataclasses.Client

class ModelFactory(val application: Application, private val client: Client) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ClientEditViewModel::class.java -> { ClientEditViewModel(application = application, client = client) as T }
            CliensEditFirstViewModel::class.java -> { CliensEditFirstViewModel(application, client) as T }
            else -> BaseViewModel(application) as T
        }
    }
}