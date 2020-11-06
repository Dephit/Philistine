package com.sergeenko.alexey.noble

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sergeenko.alexey.noble.components.AppComponent
import com.sergeenko.alexey.noble.dataclasses.Config
import com.sergeenko.alexey.noble.dataclasses.Language
import com.sergeenko.alexey.noble.dataclasses.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    fun getLanguage(): Language? = language.value

    var user: User? = null
    val appComponent: AppComponent? = (application as NobleApplication).appComponent
    val language = MutableLiveData<Language>()
    val configDao = appComponent?.getConfigDao()
    val api = appComponent?.nobleApi()
    lateinit var config: Config

    init {
        viewModelScope.launch(IO) {
            user = appComponent!!.getUser()//.userDao().getUser().firstOrNull()
            config = appComponent.getConfig() ?: Config()
            language.postValue(
                appComponent.getLanguageDao()
                    .getLanguage(config.selectedLanguageCode)
                    .firstOrNull()
                    ?.body ?: Language()
            )
            onInit()
        }
    }

    open fun onInit() {}
}