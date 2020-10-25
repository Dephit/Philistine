package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Language
import kotlinx.android.synthetic.main.activity_client_edit.*
import kotlinx.android.synthetic.main.close_button.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClientEditActivity : BaseActivity() {

    lateinit var viewModel: ClientEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_edit)
        viewModel = ModelFactory(application, intent.getSerializableExtra("client") as Client).create(ClientEditViewModel::class.java)
        observeOnViewModel()
    }
//
    fun deleteClient(view: View){
        val lang = viewModel.getLanguage()
        val builder = AlertDialog.Builder(this)
        builder
                .setPositiveButton(lang?.delete) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.deleteClient()
                    }
                }
                .setNegativeButton(lang?.decline) { _, _ -> }
                .setMessage(lang?.do_you_want_to_delete_client)
                .setTitle("")
        builder.show()
    }

    @SuppressLint("SetTextI18n")
    private fun observeOnViewModel() {
        viewModel.apply {
            language.observe(this@ClientEditActivity, {
                it.apply {
                    this@ClientEditActivity.delete_client.text = delete_client
                    close_button.button.text = close
                    screen_title.text = editing
                    setCliensInfo(client, getLanguage())
                }
            })

            isClientDeleted.observe(this@ClientEditActivity, {
                if(it)
                    onBackPressed()
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCliensInfo(client: Client, language: Language?) {
        language?.apply {
            created_text.text = "${created}: ${client.dateS?.toLongOrNull()?.let { date -> convertLongToTimeDDMMYY(date) }}"

            measure_error_layout.visibility = if(!client.trainings.isNullOrEmpty()) View.VISIBLE else View.GONE
            textView22.text = you_need_to_make_measure
            last_measure_date.text = client.lastMeasure?.dateOfMeasure?.let { convertLongToTimeDDMMYY(it) } ?: no_data
            textView24.text = last_measure
            make_measure_btn.text = make_measure
            training_amount.text = "${trainings_total_amount}: ${client.measurementsList?.size}"
            this@ClientEditActivity.measures.text = measures

            client.measurementsList?.size?.let { Toast.makeText(this@ClientEditActivity, it.toString(), Toast.LENGTH_SHORT).show() }

        }
    }
}

