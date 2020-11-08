package com.sergeenko.alexey.noble.activities

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sergeenko.alexey.noble.view_models.BaseViewModel
import com.sergeenko.alexey.noble.R
import com.sergeenko.alexey.noble.fragments.StartScreenHeaderFragment
import com.sergeenko.alexey.noble.fragments.UserListFragment
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StartActivity : BaseActivity() {

    lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        viewModel.currentState.observe(this, {
            when(it){
                is StartScreenState.NoDeviceState -> { }
                is StartScreenState.DefaultState -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.header_contained, StartScreenHeaderFragment.newInstance())
                            .replace(R.id.left_container, UserListFragment.newInstance())
                            .commit()
                }
                is StartScreenState.LoggedOutState -> {
                    startActivity(Intent(this@StartActivity, MainActivity::class.java))
                    finishAffinity()
                }
            }
        })
    }

    fun exitFromAccount(view: View){
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setPositiveButton(viewModel.getLanguage()?.exit) { _, _ ->
            viewModel.removeUser()
        }
                .setNegativeButton(viewModel.getLanguage()?.decline) { _, _ -> }
                .setMessage(viewModel.getLanguage()?.do_you_want_to_exit_account)
                .setTitle("")
        builder.show()
    }

}

sealed class StartScreenState(){
    object NoDeviceState : StartScreenState()
    object DefaultState : StartScreenState()
    object LoggedOutState : StartScreenState()

}

class StartViewModel(application: Application): BaseViewModel(application){

    val currentState = MutableLiveData<StartScreenState>(StartScreenState.DefaultState)

    fun removeUser() {
        viewModelScope.launch(IO) {
            appComponent!!.database().clearAllTables()
            user = null
            currentState.postValue(StartScreenState.LoggedOutState)
        }
    }
}