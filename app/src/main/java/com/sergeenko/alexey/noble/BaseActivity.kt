package com.sergeenko.alexey.noble

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergeenko.alexey.noble.dataclasses.ConfigDao
import com.sergeenko.alexey.noble.dataclasses.LanguageDao
import com.sergeenko.alexey.noble.dataclasses.UserDao
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(){

    @Inject
    lateinit var api: NobleApi
    lateinit var config: ConfigDao

    @Inject
    lateinit var languageDao: LanguageDao

    @Inject
    lateinit var userDao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as NobleApplication).appComponent!!.inject(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        actionBar?.let { if (it.isShowing) hideStatusBar() }
    }

    private fun hideStatusBar() {
        val decorView: View = window.decorView
        val uiOptions: Int = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        this.actionBar?.hide()
    }

    fun goBack(view: View){
        onBackPressed()
    }

}


