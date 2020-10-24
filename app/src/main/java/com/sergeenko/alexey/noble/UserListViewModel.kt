package com.sergeenko.alexey.noble

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.sergeenko.alexey.noble.dataclasses.Client
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

    init {
        viewModelScope.launch {
            getClient()
        }
    }
    val clientDao = appComponent!!.clientDao()
    val clientList = MutableLiveData<List<Client>>()
    var onDataChanged = clientDao.getClientsLiveData()

    fun getClients(name: String = ""): List<Client>? = when(sortBy){
            SortType.SirnameDesc -> clientDao.getClientsOrderBySirnameDesc(name)
            SortType.SirnameAsc -> clientDao.getClientsOrderBySirnameAsc(name)
            SortType.DateDesc -> clientDao.getClientsOrderByDateAsc(name)
            SortType.DateAsc -> clientDao.getClientsOrderByDateDesc(name)
        else -> clientDao.getClientsOrderBySirnameAsc(name)
    }

    private fun getClient(){
        user?.getFieldMap()?.let {
            api?.getClients(it)?.enqueue(object : Callback<List<Client>> {
                override fun onResponse(call: Call<List<Client>>, response: Response<List<Client>>) {
                    if(response.isSuccessful){
                        viewModelScope.launch {
                            response.body()?.map {client->
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
                list.add(if(l.size > i){l[i]}else ""
                )
            }
            val clList = getClients(list[0]) as MutableList<Client>
            for (item in list) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    clList.removeIf {
                        (getClients(item) as MutableList<Client>).find { client -> client.id == it.id } == null
                    }
                }
            }
            clientList.postValue(clList)
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