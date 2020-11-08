package com.sergeenko.alexey.noble.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.view_models.BaseViewModel
import com.sergeenko.alexey.noble.view_models.CliensEditFirstViewModel
import com.sergeenko.alexey.noble.view_models.ClientEditViewModel
import com.sergeenko.alexey.noble.view_models.ClientsEditSecondPageViewModel

class ModelFactory(val application: Application, private val client: Client) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ClientEditViewModel::class.java -> { ClientEditViewModel(application = application, client = client) as T }
            CliensEditFirstViewModel::class.java -> { CliensEditFirstViewModel(application, client) as T }
            ClientsEditSecondPageViewModel::class.java -> { ClientsEditSecondPageViewModel(application, client) as T }
            else -> BaseViewModel(application) as T
        }
    }
}