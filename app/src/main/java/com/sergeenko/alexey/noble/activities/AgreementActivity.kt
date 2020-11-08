package com.sergeenko.alexey.noble.activities

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sergeenko.alexey.noble.view_models.BaseViewModel
import com.sergeenko.alexey.noble.R
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
