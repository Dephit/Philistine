package com.sergeenko.alexey.noble

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_password_recovery.*
import kotlinx.android.synthetic.main.activity_password_recovery.view.*
import kotlinx.android.synthetic.main.close_button.view.*

class PasswordRecoveryActivity : BaseActivity() {

    lateinit var viewModel: PasswordRecoveryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recovery)
        viewModel = ViewModelProvider(this).get(PasswordRecoveryViewModel::class.java)
        observeOnViewModel()
    }

    private fun observeOnViewModel() {
        viewModel.apply {
            language.observe(this@PasswordRecoveryActivity, {
                it.apply {
                    close_button.close_button.button.text = close
                    textView3.text = password_recovery
                    textView.text = insert_your_email_and_we_send_it_to_you
                    send_button.text = send
                    name_edit.hint = email
                }
            })
            isRecoverySucceed.observe(this@PasswordRecoveryActivity, {
                viewModel.language.value?.apply {
                when(it){
                        PasswordRecoveryResponse.succsess -> {
                            Toast.makeText(this@PasswordRecoveryActivity, letter_was_sent_to_your_email, Toast.LENGTH_SHORT).show()
                            name_input.error = ""
                            onBackPressed()
                        }
                        PasswordRecoveryResponse.failure -> {
                            Toast.makeText(this@PasswordRecoveryActivity, error, Toast.LENGTH_SHORT).show()
                            name_input.error = wrong_email
                        }
                        PasswordRecoveryResponse.emptyEmail -> name_input.error = wrong_email

                    }
                }
            })
        }
    }

    fun sendPasswordRecoveryRequest(view: View){
        viewModel.sendPasswordRecoveryRequest(name_edit.text.toString())
    }
}