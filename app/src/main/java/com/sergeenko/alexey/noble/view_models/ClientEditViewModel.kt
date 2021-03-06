package com.sergeenko.alexey.noble.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sergeenko.alexey.noble.dataclasses.Client
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientEditViewModel(application: Application, val client: Client): BaseViewModel(application){

    val isClientDeleted = MutableLiveData<Boolean>()

    init {
        client.measuresFromJson()
        client.trainings()
    }

    fun deleteClient() {
        viewModelScope.launch(IO) {
            api?.deleteClient(user!!.getFieldMap(), clientId = client.id)?.enqueue(
                    object : Callback<Int> {
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            if(response.isSuccessful){
                                deleteClientFromDB()
                            }
                        }

                        override fun onFailure(call: Call<Int>, t: Throwable) {

                        }

                    }
            )
        }
    }

    private fun deleteClientFromDB() {
        viewModelScope.launch(IO) {
            appComponent?.clientDao()?.deleteClient(client)
            isClientDeleted.postValue(true)
        }
    }

}