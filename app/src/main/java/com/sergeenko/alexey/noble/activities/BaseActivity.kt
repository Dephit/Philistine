package com.sergeenko.alexey.noble.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sergeenko.alexey.noble.apis.NobleApi
import com.sergeenko.alexey.noble.NobleApplication
import com.sergeenko.alexey.noble.daos.ConfigDao
import com.sergeenko.alexey.noble.dataclasses.LanguageDao
import com.sergeenko.alexey.noble.daos.UserDao
import javax.inject.Inject

val PICK_IMAGE_REQUEST = 422

open class BaseActivity : AppCompatActivity(){

    @Inject
    lateinit var api: NobleApi
    lateinit var config: ConfigDao

    @Inject
    lateinit var languageDao: LanguageDao

    @Inject
    lateinit var userDao: UserDao

    fun openPhotoSelector(view: View){
        ImagePicker
            .with(this)
            .start(PICK_IMAGE_REQUEST)
    }

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

    fun openNewClientScreen(view: View){
        startActivity(Intent(this, NewClientActivity::class.java))
    }

    fun goBack(view: View){
        onBackPressed()
    }

}


