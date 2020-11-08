package com.sergeenko.alexey.noble.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class PasswordRecoveryResponse{
    succsess, failure, emptyEmail
}

class PasswordRecoveryViewModel(application: Application) : BaseViewModel(application) {

    val isRecoverySucceed = MutableLiveData<PasswordRecoveryResponse?>()

    fun sendPasswordRecoveryRequest(toString: String) {
        if(toString.isNotEmpty())
            api?.passwordRecovery(email = toString)?.enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if(response.isSuccessful){
                                isRecoverySucceed.postValue(PasswordRecoveryResponse.succsess)
                            }else
                                isRecoverySucceed.postValue(PasswordRecoveryResponse.failure)
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            isRecoverySucceed.postValue(PasswordRecoveryResponse.failure)
                        }

                    }
            )
        else
            isRecoverySucceed.postValue(PasswordRecoveryResponse.emptyEmail)
    }

    init {

    }
}