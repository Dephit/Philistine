package com.sergeenko.alexey.noble

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.MEDIA_TYPE_PNG
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            user?.getMultiPartField()?.let {
                val client = Client(
                        page_name = clientName,
                        sirname = surname,
                        patronymic = patronymic,
                        age = age.toString(),
                        phone = phone,
                        sex = sex,
                        bitmap = bitmap,
                        weight = weight.toString(),
                        height = clientHeight.toString()
                )
                api?.addClient(fields = it, body = client.getClientAddFormData())?.enqueue(
                        object : Callback<Int> {
                            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                if(response.isSuccessful){
                                    client.id = response.body().toString()
                                    addClientToDataBase(client)
                                }
                            }

                            override fun onFailure(call: Call<Int>, t: Throwable) {
                                
                            }
                        }
                )
            }
        }
    }

    private fun addClientToDataBase(body: Client) {
        viewModelScope.launch {
            appComponent?.clientDao()?.insertClient(body)
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