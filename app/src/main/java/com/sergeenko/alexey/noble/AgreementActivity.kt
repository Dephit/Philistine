package com.sergeenko.alexey.noble

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import kotlinx.android.synthetic.main.activity_agreement.*
import kotlinx.android.synthetic.main.activity_agreement.view.*
import kotlinx.android.synthetic.main.close_button.view.*


class AgreementActivity : BaseActivity() {

    lateinit var viewModel: AgreementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)
        viewModel = ViewModelProvider(this).get(AgreementViewModel::class.java)
        agreement_text.text = intent.getStringExtra("text")
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.apply {
            language.observe(this@AgreementActivity, {
                it.apply {
                    close_button.close_button.button.text = close

                }
            })
        }
    }
}

class AgreementViewModel(application: Application) : BaseViewModel(application) {

}
