package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.PopupWindow
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_agreement.view.*
import kotlinx.android.synthetic.main.activity_new_client.*
import kotlinx.android.synthetic.main.calendar.view.*
import kotlinx.android.synthetic.main.close_button.view.*
import kotlinx.android.synthetic.main.measure_input.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class NewClientActivity : BaseActivity() {

    lateinit var viewModel: NewClientActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)
        viewModel = ViewModelProvider(this).get(NewClientActivityViewModel::class.java)
        photo_image_view.clipToOutline = true
        male_check_box.setOnClickListener {
            male_check_box.isChecked = true
            woman_check_box.isChecked = false
        }
        woman_check_box.setOnClickListener {
            male_check_box.isChecked = false
            woman_check_box.isChecked = true
        }
        date_input.setEndIconOnClickListener {
            showCalendar()
        }
        observeOnView()
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun showCalendar() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val calendarView: View = inflater.inflate(R.layout.calendar, null)
        val mPopupWindow = PopupWindow(
                calendarView,
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
        )

        mPopupWindow.elevation = 5.0f
        calendarView.bg.setOnClickListener {
            mPopupWindow.dismiss()
        }
        calendarView.close.setOnClickListener {
            mPopupWindow.dismiss()
        }
        calendarView.confirm.setOnClickListener {
            val dOM = calendarView.datePicker.dayOfMonth
            val month = calendarView.datePicker.month
            val year = calendarView.datePicker.year
            date_input.editText?.setText("${convertStringToDate(dOM)}.${convertStringToDate(month + 1)}.$year")
            mPopupWindow.dismiss()
        }

        try {
            mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0)
        } catch (e: java.lang.Exception) {
            Log.e("ERROR_SHOWING", e.toString())
        }
    }

    fun addClient(view:View){
        viewModel.addClient()
    }

    private fun cppSetUp() {
        phone_input.name_input.name_edit.apply {
            /*ccp.registerCarrierNumberEditText(this)*/
            setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus)
                    ccp.registerCarrierNumberEditText(this)
                else ccp.deregisterCarrierNumberEditText()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (ccp.isValidFullNumber) {
                        viewModel.phone = ccp.fullNumber
                        viewModel.isPhoneNotNull()
                    }else viewModel.phone = null
                }
            })
            ccp.setCountryForNameCode(viewModel.getDefaultPhoneCode())
            ccp.setOnCountryChangeListener{
                viewModel.updateCountryCode(ccp.selectedCountryNameCode)
                ccp.registerCarrierNumberEditText(this)
                setText("")

            }
        }
    }

    private fun observeOnView() {
        viewModel.apply {
            language.observe(this@NewClientActivity, {
                cppSetUp()
                it.apply {
                    personal_info_text.text = add_new_client_button_text
                    add_photo_text.text = add_photo
                    this@NewClientActivity.volume_text.text = volume_text
                    params_text.text = params
                    close_button.button.text = close

                    surname_input.name_input.hint(surname)
                    client_name_input.name_input.hint(name)
                    patro_input.name_input.hint( second_name)
                    date_input.hint( birthday)
                    height_input.name_input.hint( height)
                    weight_input.name_input.hint( weight)
                    phone_input.name_input.hint( phone)
                    male_check_box.text = male
                    woman_check_box.text = female
                    phone_input.name_input.hint( phone)

                    left_hand_measure.name_input.hint( left_hand)
                    right_hand_measure.name_input.hint( right_hand)
                    left_hip_measure.name_input.hint( left_hip)
                    right_hip_measure.name_input.hint( right_hip)
                    hips_measure.name_input.hint( hips)
                    waist_measure.name_input.hint( waist)
                    chest_measure.name_input.hint( chest)
                    water_param.name_input.hint( water2)
                    imt_param.name_input.hint( imt)
                    fat_param.name_input.hint( fat)
                    muscle_param.name_input.hint( muscle_mass)
                    add_client_button.text = add_client

                    date_input.editText?.apply {
                        addTextChangedListener(object : TextWatcher {
                            private var current = ""
                            private val ddmmyyyy = ddmmgg
                            private val cal = Calendar.getInstance()

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                if (p0.toString() != current) {
                                    var clean = p0.toString().replace("[^\\d.]|\\.".toRegex(), "")
                                    val cleanC = current.replace("[^\\d.]|\\.", "")

                                    val cl = clean.length
                                    var sel = selectionEnd

                                    var i = 2

                                    if (clean == cleanC) sel--

                                    if (clean.length < 8) {
                                        clean += ddmmyyyy.substring(clean.length)
                                    } else {
                                        var day = Integer.parseInt(clean.substring(0, 2))
                                        var mon = Integer.parseInt(clean.substring(2, 4))
                                        var year = Integer.parseInt(clean.substring(4, 8))

                                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                                        cal.set(Calendar.MONTH, mon - 1)
                                        year = if (year < 1900) 1900 else if (year > cal.get(Calendar.YEAR)) cal.get(Calendar.YEAR) else year
                                        cal.set(Calendar.YEAR, year)

                                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                                        clean = String.format("%02d%02d%02d", day, mon, year)
                                    }

                                    clean = String.format("%s.%s.%s", clean.substring(0, 2),
                                            clean.substring(2, 4),
                                            clean.substring(4, 8))

                                    sel = if (sel < 0) 0 else sel
                                    current = clean
                                    if(sel == 2 && p0!!.length > 10)
                                        sel = 3
                                    else if(sel == 3 && p0!!.length == 10)
                                        sel = 2
                                    else if(sel == 5 && p0!!.length > 10)
                                        sel = 6
                                    else if(sel == 6  && p0!!.length == 10)
                                        sel = 5
                                    setText(current)
                                    setSelection(if (sel < current.count()) sel
                                    else current.count())
                                }
                            }

                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }

                            override fun afterTextChanged(p0: Editable) {
                                if(!current.contains(ddmmgg[0]) && !current.contains(ddmmgg[3]) && !current.contains(ddmmgg.last())){
                                    try {
                                        viewModel.age = longFromString(p0.toString())
                                        viewModel.isAgeNotNull()
                                    }catch (e: Exception){
                                        Log.e("AGE_EXCEPTION", e.message, e.fillInStackTrace())
                                    }
                                }else {
                                    viewModel.age = null
                                }
                            }
                        })
                    }
                    client_name_input.name_input.editText?.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                                }

                                override fun afterTextChanged(s: Editable?) {
                                    val string = s.toString().trim()
                                    if (string.isNotEmpty()) {
                                        viewModel.clientName = string
                                        viewModel.isClientNameNotNull()
                                    } else {
                                        viewModel.clientName = null
                                    }

                                }
                            })
                    surname_input.name_input.editText?.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                                }

                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                                }

                                override fun afterTextChanged(s: Editable?) {
                                    val string = s.toString().trim()
                                    if (string.isNotEmpty()) {
                                        viewModel.surname = string
                                        viewModel.isSurnameNotNull()
                                    } else {
                                        viewModel.surname = null
                                    }

                                }
                            })
                    patro_input.name_input.editText?.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        }

                        override fun afterTextChanged(s: Editable?) {
                            val string = s.toString().trim()
                            viewModel.patronymic = string
                        }
                    })
                    weight_input.name_input.editText?.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        }

                        override fun afterTextChanged(s: Editable?) {
                            val string = s.toString().trim()
                            viewModel.weight = string.toIntOrNull()
                        }
                    })
                    height_input.name_input.editText?.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        }

                        override fun afterTextChanged(s: Editable?) {
                            val string = s.toString().trim()
                            viewModel.clientHeight = string.toIntOrNull()
                        }
                    })
                }
            })
            dateInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, date_input.editText)
            })
            nameInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, client_name_input.name_input.editText)
                /*if(it){
                    name_input.name_input.error = " "
                }else{
                    name_input.name_input.error = ""
                }*/
            })
            surnameInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, surname_input.name_input.editText)
                //name_input.name_input
                /*if(it){
                    surname_input.name_input.error = " "
                }else{
                    surname_input.name_input.error = ""
                }*/
            })
            phoneInputError.observe(this@NewClientActivity, {
                changeActivatedState(it, phone_input.name_input.editText)
                /*if(it){
                    phone_input.name_input.error = " "
                }else{
                    phone_input.name_input.error = ""
                }*/
            })
        }
    }

    private fun changeActivatedState(it: Boolean, editText: EditText?) {
        editText?.isActivated = it
        /*if(it){
            //editText.isActivated = it
            //date_input.error = " "
        }else{
            //date_input.error = ""
        }*/
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
            } catch (e: IOException) {
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
