package com.sergeenko.alexey.noble

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sergeenko.alexey.noble.dataclasses.Client
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class SortType{
    Alpha, Date,
    SirnameDesc, SirnameAsc, DateDesc, DateAsc
}

class UserListViewModel(application: Application) : BaseViewModel(application) {

    var sortBy = SortType.DateAsc

    override fun onInit(){
        getClient()
    }

    val clientDao = appComponent!!.clientDao()
    val clientList = MutableLiveData<List<Client>>()
    var onDataChanged = clientDao.getClientsLiveData()

    private fun getClients(name: String = "", surname: String = ""): List<Client>? = when(sortBy){
            SortType.SirnameDesc -> clientDao.getClientsOrderBySirnameDesc(name, surname)
            SortType.SirnameAsc -> clientDao.getClientsOrderBySirnameAsc(name, surname)
            SortType.DateDesc -> clientDao.getClientsOrderByDateDesc(name, surname)
            SortType.DateAsc -> clientDao.getClientsOrderByDateAsc(name, surname)
        else -> clientDao.getClientsOrderBySirnameAsc(name, surname)
    }

    private fun getClients(name: String = "", surname: String = "", patro: String = ""): List<Client>? = when(sortBy){
        SortType.SirnameDesc -> clientDao.getClientsOrderBySirnameDesc(name, surname, patro)
        SortType.SirnameAsc -> clientDao.getClientsOrderBySirnameAsc(name, surname, patro)
        SortType.DateDesc -> clientDao.getClientsOrderByDateDesc(name, surname, patro)
        SortType.DateAsc -> clientDao.getClientsOrderByDateAsc(name, surname, patro)
        else -> clientDao.getClientsOrderBySirnameAsc(name, surname)
    }

    fun getClients(name: String = ""): List<Client>? = when(sortBy){
        SortType.SirnameDesc -> clientDao.getClientsOrderBySirnameDesc(name)
        SortType.SirnameAsc -> clientDao.getClientsOrderBySirnameAsc(name)
        SortType.DateDesc -> clientDao.getClientsOrderByDateDesc(name)
        SortType.DateAsc -> clientDao.getClientsOrderByDateAsc(name)
        else -> clientDao.getClientsOrderBySirnameAsc(name)
    }

    private fun getClient(){
        /*if(user == null)
            user = appComponent!!.userDao().getUser()*/
        user?.getFieldMap()?.let {
            api?.getClients(it)?.enqueue(object : Callback<List<Client>> {
                override fun onResponse(call: Call<List<Client>>, response: Response<List<Client>>) {
                    if(response.isSuccessful){
                        viewModelScope.launch {
                            response.body()?.map {client->
                                /*client.trainings()
                                client.measuresFromJson()*/
                                client.getLastSession()
                                clientDao.insertClient(client)
                            }
                            clientList.postValue(getClients())
                        }
                    }else {
                        clientList.postValue(getClients())
                    }
                }

                override fun onFailure(call: Call<List<Client>>, t: Throwable) {
                    viewModelScope.launch {
                        clientList.postValue(getClients())
                    }
                }
            })
        }
    }

    fun searchClients(name: String) {
        viewModelScope.launch {
            val text = name.trim()
            val list = mutableListOf<String>()
            val l = text.split(" ")
            for (i in 0 until 3){
                if(l.size > i){
                    list.add(l[i])
                }

            }



            clientList.postValue(when (list.size) {
                1 -> getClients(list[0])
                2 -> getClients(list[0], list[1])
                else -> getClients(list[0], list[1], list[2])
            })
        }
    }

    fun sortBy(alpha1: String, alpha: SortType) {
        sortBy = if(alpha == SortType.Alpha){
            if (sortBy == SortType.SirnameDesc)
                SortType.SirnameAsc
            else SortType.SirnameDesc
        }else {
            if (sortBy == SortType.DateDesc)
                SortType.DateAsc
            else SortType.DateDesc
        }
        searchClients(alpha1)
    }
}