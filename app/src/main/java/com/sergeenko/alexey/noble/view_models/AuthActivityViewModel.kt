package com.sergeenko.alexey.noble.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sergeenko.alexey.noble.daos.UserDao
import com.sergeenko.alexey.noble.dataclasses.*
import com.sergeenko.alexey.noble.modules.NoInternetException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import java.util.regex.Pattern

sealed class AuthState{
    object LoggingState: AuthState()
    object LoggedState: AuthState()
    object DefaultState: AuthState()
    class ErrorState(val error: AuthErrorType, val message: String? = null) : AuthState()
}

enum class AuthErrorType{
    EmailError, PasswordError, NetworkError, AgreementError
}

class AuthActivityViewModel(application: Application) : BaseViewModel(application) {
    val authState = MutableLiveData<AuthState>(AuthState.DefaultState)
    val langList = MutableLiveData<List<LangList>?>()
    val isListOpen = MutableLiveData<Boolean>(false)
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
        authState.postValue(AuthState.LoggingState)
        if(email.isEmpty() || !isValidEmailAddress(email)){
            authState.postValue(AuthState.ErrorState(AuthErrorType.EmailError))
            return
        }else if(password.isEmpty()){
            authState.postValue(AuthState.ErrorState(AuthErrorType.PasswordError))
            return
        }else if(!isAgreementSigned){
            authState.postValue(AuthState.ErrorState(AuthErrorType.AgreementError, getLanguage()?.you_must_accept_user_agreement))
            return
        }
        auth(email, password)
    }

    private fun auth(email: String, password: String) = viewModelScope.launch(IO) {
        try {
            api?.auth(email = email, password = password)?.let {club ->
                user = User(email = email, lastTimeSync = Calendar.getInstance().time.time, pass = password, club = club)
                addUser(user!!)
            } ?: authError()
        }catch (e: Exception){
            getLanguage()?.apply {
                when (e) {
                    is NoInternetException -> authError(check_internet_connection)
                    else -> authError(e.message)
                }
            }
        }
    }

    private fun authError(message: String? = null) {
        authState.postValue(AuthState.ErrorState(AuthErrorType.NetworkError, message = message))
    }

    private fun addUser(user: User) {
        userDao?.deleteTable()
        userDao?.insertUser(user)
        authState.postValue(AuthState.LoggedState)
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

