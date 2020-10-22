package com.sergeenko.alexey.noble

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

class NewClientActivityViewModel(application: Application) : BaseViewModel(application){

    var bitmap: Bitmap? = null
    var age: Long? = null
    var phone: String? = null
    var clientName: String? = null
    var surname: String? = null
    var patronymic: String? = null
    var sex: String? = null
    var clientHeight: Int? = null
    var weight: Int? = null

    var dateInputError = MutableLiveData<Boolean>()
    var nameInputError = MutableLiveData<Boolean>()
    var phoneInputError = MutableLiveData<Boolean>()
    var surnameInputError = MutableLiveData<Boolean>()

    fun getDefaultPhoneCode(): String = config.countryCode

    fun updateCountryCode(selectedCountryNameCode: String?) {
        viewModelScope.launch {
            selectedCountryNameCode?.let{
                config.countryCode = it
                configDao?.updateConfig(config)
            }
        }
    }

    fun addClient(){
        val isNameCorrect = isSurnameNotNull()
        val isSurnameCorrect = isClientNameNotNull()
        val isPhoneCorrect = isPhoneNotNull()
        val isAgeCorrect = isAgeNotNull()
        if(isAgeCorrect && isNameCorrect && isPhoneCorrect && isSurnameCorrect){

        }
    }

    fun isSurnameNotNull(): Boolean {
        surnameInputError.postValue(surname == null)
        return surname != null
    }

    fun isClientNameNotNull(): Boolean {
        nameInputError.postValue(clientName == null)
        return clientName != null
    }

    fun isAgeNotNull(): Boolean {
        dateInputError.postValue(age == null)
        return age != null
    }

    fun isPhoneNotNull(): Boolean {
        phoneInputError.postValue(phone == null)
        return phone != null
    }

    fun setImage(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }
}