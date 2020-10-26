package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import com.sergeenko.alexey.noble.dataclasses.Language
import kotlinx.android.synthetic.main.activity_new_client.*
import kotlinx.android.synthetic.main.close_button.view.*
import kotlinx.android.synthetic.main.measure_input.view.*
import kotlinx.android.synthetic.main.measure_input.view.name_edit
import kotlinx.android.synthetic.main.personal_info_layout.*
import kotlinx.android.synthetic.main.personal_info_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlinx.android.synthetic.main.personal_info_layout.personal_info
import kotlinx.android.synthetic.main.personal_info_layout.view.ccp

class NewClientActivity : BaseActivity() {

    lateinit var viewModel: NewClientActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)
        viewModel = ViewModelProvider(this).get(NewClientActivityViewModel::class.java)
        add_photo_text.visibility = View.VISIBLE
        replace_image_layout.visibility = View.INVISIBLE
        observeOnView()
    }

    fun addClient(view:View){
        viewModel.addClient()
    }

    private fun observeOnView() {
        viewModel.apply {
            language.observe(this@NewClientActivity, {
                it.apply {
                    close_button.button.text = close

                    personal_info.setPersonalInfo(viewModel,this)
                    left_hand_measure.name_input.hint(left_hand)
                    right_hand_measure.name_input.hint(right_hand)
                    left_hip_measure.name_input.hint(left_hip)
                    right_hip_measure.name_input.hint(right_hip)
                    hips_measure.name_input.hint(hips)
                    waist_measure.name_input.hint(waist)
                    chest_measure.name_input.hint(chest)
                    water_param.name_input.hint(water2)
                    imt_param.name_input.hint(imt)
                    fat_param.name_input.hint(fat)
                    muscle_param.name_input.hint(muscle_mass)
                    add_client_button.text = add_client
                    addTextWrapers(ddmmgg)
                }
            })
            dateInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, date_input.editText)
            })
            nameInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, client_name_input.name_input.editText)
            })
            surnameInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, surname_input.name_input.editText)
            })
            phoneInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, phone_input.name_input.editText)
            })
        }
    }

    private fun addTextWrapers(ddmmgg: String) {
        left_hand_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setLeftHandVolume(s.toString().trim())
        })
        right_hand_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setRightHandVolume(s.toString().trim())
        })
        left_hip_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setLeftHipVolume(s.toString().trim())
        })
        right_hip_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setRightHipVolume(s.toString().trim())
        })
        hips_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setHipsVolume(s.toString().trim())
        })
        waist_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setWaistVolume(s.toString().trim())
        })
        chest_measure.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setChestVolume(s.toString().trim())
        })

        imt_param.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setImt(s.toString().trim())
        })
        muscle_param.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setMuscleMass(s.toString().trim())
        })
        fat_param.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setFat(s.toString().trim())
        })
        water_param.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setWater(s.toString().trim())
        })
        weight_input.name_input.editText?.addTextChangedListener(afterTextChanged = {s->
            viewModel.setMeasureWeight(s.toString().trim())
        })
    }

    private fun changeActivatedState(it: Boolean, editText: EditText?) {
        editText?.isActivated = it
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            try {
                val selectedImage = data?.data!!
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                viewModel.setImage(bitmap)
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
}

@SuppressLint("UseCompatLoadingForDrawables")
fun TextInputLayout.hint(string: String){
    CoroutineScope(IO).launch {
        /*editText?.background = context.getDrawable(
                when {
                    string.length <= 5 ->  { R.drawable.measur_bg_5 }
                    string.length <= 10 -> { R.drawable.measur_bg_10 }
                    string.length <= 15 -> { R.drawable.measur_bg_15 }
                    string.length <= 20 -> { R.drawable.measur_bg_20 }
                    string.length <= 25 -> { R.drawable.measur_bg_25 }
                    string.length <= 30 -> { R.drawable.measur_bg_30 }
                    else -> { R.drawable.measur_bg }
                }
        )*/
        hint = string
    }
}