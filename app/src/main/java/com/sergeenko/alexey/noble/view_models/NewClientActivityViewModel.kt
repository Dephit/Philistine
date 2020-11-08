package com.sergeenko.alexey.noble.view_models

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hbb20.CountryCodePicker
import com.sergeenko.alexey.noble.interfaces.ClientInfoInput
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Measure
import com.sergeenko.alexey.noble.utills.longFromString
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewClientActivityViewModel(application: Application) : BaseViewModel(application), ClientInfoInput {

    val client = Client()
    private val measure = Measure()

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

    fun addClient(){
        val isNameCorrect = isSurnameNotNull()
        val isSurnameCorrect = isClientNameNotNull()
        val isPhoneCorrect = isPhoneNotNull()
        val isAgeCorrect = isAgeNotNull()
        if(isAgeCorrect && isNameCorrect && isPhoneCorrect && isSurnameCorrect){
            user?.getMultiPartField()?.let {
                if(measure.isNotEmpty()){
                    client.addNewMeasure(measure = measure)
                }
                api?.addClient(fields = it, body = client.getClientAddFormData())?.enqueue(
                        object : Callback<Int> {
                            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                                if(response.isSuccessful){
                                    client.id = response.body().toString()
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
    }

    private fun addClientToDataBase(body: Client) {
        viewModelScope.launch(IO) {
            appComponent?.clientDao()?.insertClient(body)
        }
    }

    override fun isSurnameNotNull(): Boolean {
        surnameInputError.postValue(client.sirname == null)
        return client.sirname != null
    }

    override fun isClientNameNotNull(): Boolean {
        nameInputError.postValue(client.page_name == null)
        return client.page_name != null
    }

    override fun isAgeNotNull(): Boolean {
        dateInputError.postValue(client.age == null)
        return client.age != null
    }

    override fun isPhoneNotNull(): Boolean {
        phoneInputError.postValue(client.phone == null)
        return client.phone != null
    }

    fun setImage(bitmap: Bitmap?) {
        client.bitmap = bitmap
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

    override fun setName(string: String) {
        if (string.isNotEmpty()) {
            client.page_name = string
            isClientNameNotNull()
        } else {
            client.page_name = null
        }
    }

    override fun setSurname(string: String) {
        if (string.isNotEmpty()) {
            client.sirname = string
            isSurnameNotNull()
        } else {
            client.sirname = null
        }
    }

    override fun setPatronymic(string: String) {
        client.patronymic = string
    }

    override fun setWeight(trim: String) {
        client.weight = trim.toIntOrNull()?.toString()
    }

    override fun setHeight(trim: String) {
        client.height = trim.toIntOrNull()?.toString()
    }

    override fun setSex(trim: String) {
        client.sex = trim
    }

    fun setMeasureWeight(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.params.weight = toIntOrNull()
                client.weight = toIntOrNull().toString()
            }
        }
    }

    fun setLeftHandVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.leftHandVolume = toIntOrNull()
            }
        }
    }

    fun setRightHandVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.rightHandVolume = toIntOrNull()
            }
        }
    }

    fun setLeftHipVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.leftHipVolume = toIntOrNull()
            }
        }
    }

    fun setRightHipVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.rightHipVolume = toIntOrNull()
            }
        }
    }

    fun setHipsVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.hipsVolume = toIntOrNull()
            }
        }
    }

    fun setWaistVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.waistVolume = toIntOrNull()
            }
        }
    }

    fun setChestVolume(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.measures.chestVolume = toIntOrNull()
            }
        }
    }

    fun setImt(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.params.imt = toIntOrNull()
            }
        }
    }

    fun setMuscleMass(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.params.muscleMass = toIntOrNull()
            }
        }
    }

    fun setFat(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.params.fatTisue = toIntOrNull()
            }
        }
    }

    fun setWater(trim: String) {
        trim.apply {
            if(isNotEmpty()) {
                measure.params.waterInBody = toIntOrNull()
            }
        }
    }


}