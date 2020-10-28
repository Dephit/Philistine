package com.sergeenko.alexey.noble

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sergeenko.alexey.noble.dataclasses.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern

class AuthActivityViewModel(application: Application) : BaseViewModel(application) {

    val langList = MutableLiveData<List<LangList>?>()
    val isListOpen = MutableLiveData<Boolean>(false)
    val nameErrorLiveData = MutableLiveData<String?>()
    val passwordErrorLiveData = MutableLiveData<String?>()
    val isAuthSuccessful = MutableLiveData<Boolean>(false)
    var isAgreementSigned = false

    private val languageDao: LanguageDao? = appComponent?.getLanguageDao()
    private val userDao: UserDao? = appComponent?.userDao()

    init {
        getLanguages()
    }

    private fun getLanguages() {
        viewModelScope.launch(IO) {
            langList.postValue(languageDao?.getLanguage())
        }
    }

    fun changeListVisibility() {
        isListOpen.postValue(!isListOpen.value!!)
    }

    fun getConfigLanguage(): String {
        return config.selectedLanguageCode
    }

    fun signIn(email: String, password: String) {
        fun emailCorrect() = email.isNotEmpty() && isValidEmailAddress(email)
        fun passwordCorrect() = password.isNotEmpty()
        language.value?.apply {
            if(emailCorrect() && passwordCorrect() && isAgreementSigned){
                auth(email, password)
            }
            nameErrorLiveData.postValue(if(!emailCorrect()) wrong_email else null)
            passwordErrorLiveData.postValue(if(!passwordCorrect()) wrong_password else null)
            }
    }

    private fun auth(email: String, password: String) {
        api?.auth(email = email, password = password)?.enqueue(
            object : Callback<Club> {
                override fun onResponse(call: Call<Club>, response: Response<Club>) {
                    Log.i("dasdasdasd", response.message())
                    if(response.isSuccessful){
                        viewModelScope.launch(IO) {
                            user = User(email = email, lastTimeSync = Calendar.getInstance().time.time, pass = password, club = response.body())
                            addUser(user!!)
                        }
                    }else{
                        authError()
                    }
                }

                override fun onFailure(call: Call<Club>, t: Throwable) {
                    authError()
                }

            }
        )
    }

    private fun authError() {
        nameErrorLiveData.postValue(language.value?.wrong_email)
        passwordErrorLiveData.postValue(language.value?.wrong_password)
        isAuthSuccessful.postValue(false)
    }

    private fun addUser(user: User) {
        userDao?.deleteTable()
        userDao?.insertUser(user)
        isAuthSuccessful.postValue(true)
    }


    private fun isValidEmailAddress(email: String?): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    fun agree(checked: Boolean) {
        isAgreementSigned = checked
    }
}

