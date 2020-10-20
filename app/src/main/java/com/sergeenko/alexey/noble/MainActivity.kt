package com.sergeenko.alexey.noble

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.LangList
import com.sergeenko.alexey.noble.dataclasses.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(IO + viewModelJob)

    @Inject
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLanguages()
    }

    private fun getLanguages() {
        api.getLangList().enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                viewModelScope.launch {
                    response.body()?.let {
                        getLang(JSONObject(it.string()))
                        goToLaunchScreen()
                    } ?: goToLaunchScreen()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                viewModelScope.launch {
                    goToLaunchScreen()
                }
            }
        })
    }

    private suspend fun goToLaunchScreen() {
        userDao.getUser().apply {
            firstOrNull()?.let { user->
                val time = Calendar.getInstance().time.time
                /*if(time - user.lastTimeSync >= 2592000L){
                    //SyncScreen
                }else {*/
                    startActivity(Intent(this@MainActivity, StartActivity::class.java))
                //}
            } ?: startActivity(Intent(this@MainActivity, AuthActivity::class.java))
        }
        finish()
    }

    private suspend fun getLang(jsonObject: JSONObject) {
        jsonObject.keys().forEach {
            api.getLanguage(variable = it).awaitResponse().apply {
                if(isSuccessful){
                    this.body()?.let {lang->
                        languageDao.insertLanguage(LangList(it, lang))
                    }
                }
            }
        }
    }
}