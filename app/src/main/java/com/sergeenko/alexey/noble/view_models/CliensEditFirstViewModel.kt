package com.sergeenko.alexey.noble.view_models

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hbb20.CountryCodePicker
import com.sergeenko.alexey.noble.interfaces.ClientInfoInput
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.utills.longFromString
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CliensEditFirstViewModel(application: Application,val client: Client) : BaseViewModel(application), ClientInfoInput {

    var dateInputError = MutableLiveData<Boolean>()
    var nameInputError = MutableLiveData<Boolean>()
    var phoneInputError = MutableLiveData<Boolean>()
    var surnameInputError = MutableLiveData<Boolean>()

    var isClientSuccessivelyAdded = MutableLiveData<Boolean>()

    override fun setPhone(ccp: CountryCodePicker?) {
        if (ccp!!.isValidFullNumber) {
            client.phone = ccp.fullNumber
            isPhoneNotNull()
        }else client.phone = null
    }

    override fun getDefaultPhoneCode(): String = config.countryCode

    override fun updateCountryCode(selectedCountryNameCode: String?) {
        viewModelScope.launch(IO) {
            selectedCountryNameCode?.let{
                config.countryCode = it
                configDao?.updateConfig(config)
            }
        }
    }

    override fun isAgeNotNull(): Boolean {
        dateInputError.postValue(client.age == null)
        return client.age != null
    }

    override fun setAge(current: String, edittable: String) {
        val ddmmgg = getLanguage()!!.ddmmgg
        if(!current.contains(ddmmgg[0]) && !current.contains(ddmmgg[3]) && !current.contains(ddmmgg.last())){
            try {
                client.age = longFromString(current)
                isAgeNotNull()
            }catch (e: Exception){
                Log.e("AGE_EXCEPTION", e.message, e.fillInStackTrace())
            }
        }else {
            client.age = null
        }
    }

    override fun setName(trim: String) {
        if (trim.isNotEmpty()) {
            client.page_name = trim
            isClientNameNotNull()
        } else {
            client.page_name = null
        }
    }

    override fun setSex(trim: String) {
        client.sex = trim
    }

    override fun isClientNameNotNull(): Boolean {
         nameInputError.postValue(client.page_name == null)
         return client.page_name != null
    }

    override fun isSurnameNotNull(): Boolean {
        surnameInputError.postValue(client.sirname == null)
        return client.sirname != null
    }

    override fun setSurname(trim: String) {
        if (trim.isNotEmpty()) {
            client.sirname = trim
            isSurnameNotNull()
        } else {
            client.sirname = null
        }
    }

    override fun setPatronymic(trim: String) {
        client.patronymic = trim
    }

    override fun setWeight(trim: String) {
        client.weight = trim.toIntOrNull()?.toString()
    }

    override fun setHeight(trim: String) {
        client.height = trim.toIntOrNull()?.toString()
    }

    override fun isPhoneNotNull(): Boolean {
        phoneInputError.postValue(client.phone == null)
        return client.phone != null
    }

    fun saveClientChanges() {
        user?.getMultiPartField()?.let {
            api?.editClient(fields = it, body = client.getClientAddFormData())?.enqueue(
                object : Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if(response.isSuccessful){
                            client.bitmap = null
                            addClientToDataBase(client)
                            isClientSuccessivelyAdded.postValue(true)
                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        isClientSuccessivelyAdded.postValue(false)
                    }
                }
            )
        }
    }

    private fun addClientToDataBase(client: Client) {
        viewModelScope.launch(IO) {
            appComponent?.clientDao()?.updateClient(client)
        }
    }

    fun setImage(bitmap: Bitmap?) {
        client.bitmap = bitmap
    }

}