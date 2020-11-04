package com.sergeenko.alexey.noble

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.RemoteException
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.sergeenko.alexey.noble.dataclasses.Client
import com.sergeenko.alexey.noble.dataclasses.Language
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.calendar.view.*
import kotlinx.android.synthetic.main.measure_input.view.*
import kotlinx.android.synthetic.main.personal_info_layout.view.*
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.personal_info_layout.view.name_edit as name_edit1

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun makeAndShowToast(context: WeakReference<Context>, message: String = "", lenght: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context.get(), message, lenght).show()
}

fun hideKeyboard(activity: WeakReference<Activity>?) {
    activity?.get()?.apply {
        val inputManager: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val v = currentFocus ?: return
        inputManager?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun getAgeFromLong(dateOfBirthLong: Long) = getDiffYears(dateOfBirthLong * 1000, Calendar.getInstance().time.time).toString()

fun convertStringToDate(get: Int) = ("00${get}").substring(get.toString().length)

fun getMuscleName(lang: Language, value: String) = when(value){
    "Кардио" -> lang.cardio
    "Лимфодренажный массаж" -> lang.limfo_massage
    "Силовой" -> lang.strength_mode
    "Расслабляющий массаж" -> lang.relax_massage
    "Тонус мышц" -> lang.muscle_tonic
    "Восстанавливающий массаж" -> lang.recovery_massage
    "Антицеллюлитный массаж" -> lang.anticellulite_massage
    "Укрепляющий массаж" -> lang.firming_massage
    "Снижение веса" -> lang.weight_loss
    "Ручные настройки" -> lang.manual_settings
      else -> value
  }

fun longFromString(toString: String): Long {
    val day = toString.substring(0, 2)
    val month = toString.substring(3, 5)
    val year = toString.substring(6, toString.length)

    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year.toInt())
    cal.set(Calendar.MONTH, month.toInt() - 1)
    cal.set(Calendar.DAY_OF_MONTH, day.toInt())

    return cal.time.time / 1000
}

private fun getDiffYears(first: Long?, last: Long): Int {
    val a: Calendar = getCalendar(first)
    val b: Calendar = getCalendar(last)
    var diff = b[Calendar.YEAR] - a[Calendar.YEAR]
    if (a[Calendar.MONTH] > b[Calendar.MONTH] ||
            a[Calendar.MONTH] == b[Calendar.MONTH] && a[Calendar.DATE] > b[Calendar.DATE]
    ) {
        diff--
    }
    return diff
}

private fun getCalendar(date: Long?): Calendar {
    val cal = Calendar.getInstance()
    cal.time = Date(date!!)
    return cal
}

fun Bitmap.toByteArray(): ByteArray{
    val bao = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 50, bao)
    return bao.toByteArray()
}

fun String.stringToByteArray(): ByteArray?{
    return try {
        val split: List<String> = substring(1, length - 1).split(", ")
        val array = ByteArray(split.size)
        for (i in split.indices) {
            array[i] = split[i].trim().toByte()
        }
        array
    }catch (e: Exception){
        null
    }
}

fun showCalendarView(context: WeakReference<Context>, view: View, lang: Language, func: (day: Int, month: Int, year: Int) -> Unit) {
    context.get()?.let {
        val inflater = it.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val calendarView: View = inflater.inflate(R.layout.calendar, null)
        val mPopupWindow = PopupWindow(calendarView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        mPopupWindow.elevation = 5.0f
        mPopupWindow.isFocusable = true
        mPopupWindow.isOutsideTouchable = true

        calendarView.close.text = lang.close
        calendarView.confirm.text = "Ок"

        calendarView.bg.setOnClickListener {
            mPopupWindow.dismiss()
        }
        calendarView.close.setOnClickListener {
            mPopupWindow.dismiss()
        }
        calendarView.confirm.setOnClickListener {
            func(calendarView.datePicker.dayOfMonth, calendarView.datePicker.month, calendarView.datePicker.year)
            mPopupWindow.dismiss()
        }
        try {
            mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        } catch (e: java.lang.Exception) {
            Log.e("ERROR_SHOWING", e.toString())
        }
    }
}

fun convertLongToTimeDDMMYY(time: Long) = convertLongToTimeWithFormat(time, "dd.MM.yyyy")

fun convertLongToTimeDDMM(time: Long) = convertLongToTimeWithFormat(time, "dd.MM")

private fun convertLongToTimeWithFormat(time: Long, format: String = "dd.MM.yyyy"): String = SimpleDateFormat(format, Locale.US).format(Date(time * 1000))

fun replaceQuoits(get: String): String {
    return try {
        get
            .replace("&quot;", "\"")
            .replace("\\", "")
            .replace("[\"{", "[{")
            .replace("}\"]", "}]")
            .replace("\"\"", "0")
            .also { Log.i("REPLACE_SRING", JSONArray(it).toString()) }
    }catch (e: Exception){
        ""
    }

}

@SuppressLint("UseCompatLoadingForDrawables")
fun ConstraintLayout.fillPersonalInfo(client: Client){
    height_input.name_input.editText?.setText(client.height)
    weight_input.name_input.editText?.setText(client.weight)

    patro_input.name_input.editText?.setText(client.patronymic)
    surname_input.name_input.editText?.setText(client.sirname)
    client_name_input.name_input.editText?.setText(client.page_name)
    date_input.editText?.setText(convertLongToTimeDDMMYY(client.age!!))
    ccp.fullNumber = client.phone
    if(client.sex == "man"){
        male_check_box.callOnClick()
    }else if(client.sex == "woman"){
        woman_check_box.callOnClick()
    }

    Picasso.with(context)
        .load("http://noble.gensol.ru/files/${client.foto}")
        .fit()
        .placeholder(R.drawable.client_image_place_holder)
        .error(client.bitmap?.let {
            BitmapDrawable(resources, client.bitmap)
        } ?: context.getDrawable(R.drawable.client_image_place_holder))
        .into(photo_image_view, object : Callback {
            override fun onSuccess() {
                replace_image_layout.visibility = View.VISIBLE
                add_photo_text.visibility = View.INVISIBLE
            }

            override fun onError() {
                add_photo_text.visibility = View.VISIBLE
                replace_image_layout.visibility = View.INVISIBLE
            }

        })

}

@SuppressLint("SetTextI18n")
fun ConstraintLayout.setPersonalInfo(viewModel: ClientInfoInput, lang: Language){
    fun cppSetUp() {
        val ccp = personal_info.ccp
        phone_input.name_input.name_edit.apply {
            setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) ccp.registerCarrierNumberEditText(this) else ccp.deregisterCarrierNumberEditText()
            }
            addTextChangedListener { doOnTextChanged { _, _, _, _ -> viewModel.setPhone(ccp) }}
            ccp.setCountryForNameCode(viewModel.getDefaultPhoneCode())
            ccp.setOnCountryChangeListener{
                viewModel.updateCountryCode(ccp.selectedCountryNameCode)
                ccp.registerCarrierNumberEditText(this)
                setText("")
            }
        }
        date_input.editText?.apply {
            addTextChangedListener(object : TextWatcher {
                private var current = ""
                private val ddmmyyyy = lang.ddmmgg
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
                            year = if (year < 1900) 1900 else if (year > cal.get(Calendar.YEAR)) cal.get(
                                    Calendar.YEAR) else year
                            cal.set(Calendar.YEAR, year)

                            day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                                    Calendar.DATE) else day
                            clean = String.format("%02d%02d%02d", day, mon, year)
                        }

                        clean = String.format("%s.%s.%s", clean.substring(0, 2),
                                clean.substring(2, 4),
                                clean.substring(4, 8))

                        sel = if (sel < 0) 0 else sel
                        current = clean
                        if (sel == 2 && p0!!.length > 10)
                            sel = 3
                        else if (sel == 3 && p0!!.length == 10)
                            sel = 2
                        else if (sel == 5 && p0!!.length > 10)
                            sel = 6
                        else if (sel == 6 && p0!!.length == 10)
                            sel = 5
                        setText(current)
                        setSelection(if (sel < current.count()) sel
                        else current.count())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable) {
                    viewModel.setAge(current, p0.toString())

                }
            })
        }
        client_name_input.name_input.editText?.addTextChangedListener(afterTextChanged = { s ->
            viewModel.setName(s.toString().trim())
        })
        surname_input.name_input.editText?.addTextChangedListener(afterTextChanged = { s ->
            viewModel.setSurname(s.toString().trim())
        })
        patro_input.name_input.editText?.addTextChangedListener(afterTextChanged = { s ->
            viewModel.setPatronymic(s.toString().trim())
        })
        weight_input.name_input.editText?.addTextChangedListener(afterTextChanged = { s ->
            viewModel.setWeight(s.toString().trim())

        })
        height_input.name_input.editText?.addTextChangedListener(afterTextChanged = { s ->
            viewModel.setHeight(s.toString().trim())
        })
    }

    lang.apply{
        cppSetUp()
        personal_info_text.text = personal_information
        add_photo_text.text = add_photo
        replace_image_layout.text = replace_photo

        surname_input.name_input.hint(surname)
        client_name_input.name_input.hint(name)
        patro_input.name_input.hint(second_name)
        date_input.hint(birthday)
        height_input.name_input.hint(height)
        weight_input.name_input.hint(weight)
        phone_input.name_input.hint(phone)
        male_check_box.text = male
        woman_check_box.text = female
        phone_input.name_input.hint(phone)
    }

    photo_image_view.clipToOutline = true
    male_check_box.setOnClickListener {
        viewModel.setSex("man")
        male_check_box.isChecked = true
        woman_check_box.isChecked = false
    }
    woman_check_box.setOnClickListener {
        viewModel.setSex("woman")
        male_check_box.isChecked = false
        woman_check_box.isChecked = true
    }
    date_input.setEndIconOnClickListener {
        showCalendarView(WeakReference(context), this, lang) { day, month, year ->
            date_input.editText?.setText("${convertStringToDate(day)}.${convertStringToDate(month + 1)}.$year")
        }
    }
}