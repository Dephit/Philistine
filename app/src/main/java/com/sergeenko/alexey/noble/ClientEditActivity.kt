package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Language
import kotlinx.android.synthetic.main.activity_client_edit.*
import kotlinx.android.synthetic.main.close_button.view.*
import kotlinx.android.synthetic.main.personal_info_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.system.measureTimeMillis

class ClientEditActivity : BaseActivity() {

    lateinit var viewModel: ClientEditViewModel
    var firstFragment: CliensEditFirstFragment? = null
    var secondFragment: ClientsEditSecondPage? = null
    var thirdFragment: ClientEditThirdFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_edit)
        viewModel = ModelFactory(application, intent.getSerializableExtra("client") as Client).create(ClientEditViewModel::class.java)
        observeOnViewModel()
        setFirstPage(client_cart)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            try {
                val selectedImage = data?.data!!
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                firstFragment?.setImage(bitmap)
                photo_image_view.setImageBitmap(bitmap)
                add_photo_text.visibility = View.INVISIBLE
                replace_image_layout.visibility = View.VISIBLE
            } catch (e: IOException) {
                replace_image_layout.visibility = View.INVISIBLE
                add_photo_text.visibility = View.VISIBLE
                e.printStackTrace()
            }

        }
    }

    fun setFirstPage(view: View) {
        setSelectedTab(0)
        if(firstFragment == null)
            firstFragment = CliensEditFirstFragment.newInstance(viewModel.client)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_placement, firstFragment!!)
            .commit()
    }

    private fun setSelectedTab(i: Int) {
        client_cart.isActivated = i == 0
        training_amount.isActivated = i == 1
        measures.isActivated = i == 2

    }

    fun saveClientChanges(view: View){
        firstFragment?.saveClientChanges()
    }

    fun setSecondPage(view: View) {
        setSelectedTab(1)
        if(secondFragment == null)
            secondFragment = ClientsEditSecondPage.newInstance(viewModel.client)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, secondFragment!!)
                .commit()
    }

    fun setThirdPage(view: View) {
        setSelectedTab(2)
        if(thirdFragment == null)
            thirdFragment = ClientEditThirdFragment.newInstance(viewModel.client)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, thirdFragment!!)
                .commit()
    }

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

            this@ClientEditActivity.client_cart.text = client_cart
            measure_error_layout.visibility = if(client.trainingList!!.size % 5 == 0) View.VISIBLE else View.GONE
            close_message.setOnClickListener {
                measure_error_layout.visibility = View.GONE
            }
            textView22.text = you_need_to_make_measure
            last_measure_date.text = client.measurementsList?.lastOrNull()?.dateOfMeasure?.let { convertLongToTimeDDMMYY(it) } ?: no_data
            textView24.text = last_measure
            make_measure_btn.text = make_measure
            training_amount.text = "${trainings_total_amount}: ${client.trainingList?.size}"
            this@ClientEditActivity.measures.text = measures
        }
    }
}

