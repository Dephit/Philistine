package com.sergeenko.alexey.noble

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.User
import kotlinx.android.synthetic.main.user_list_fragment.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : BaseActivity() {

    lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        supportFragmentManager.beginTransaction()
            .replace(R.id.header_contained, StartScreenHeaderFragment.newInstance())
            .replace(R.id.left_container, UserListFragment.newInstance())
            .commit()

    }



    fun exitFromAccount(view: View){
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setPositiveButton(viewModel.getLanguage()?.exit) { _, _ ->
                    viewModel.viewModelScope.launch(IO) {
                        viewModel.removeUser()
                        startActivity(Intent(this@StartActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                }
                .setNegativeButton(viewModel.getLanguage()?.decline) { _, _ -> }
                .setMessage(viewModel.getLanguage()?.do_you_want_to_exit_account)
                .setTitle("")
        builder.show()
    }

}

class StartViewModel(application: Application): BaseViewModel(application){


    suspend fun removeUser() {
        appComponent!!.database().clearAllTables()
        user = null
    }

}