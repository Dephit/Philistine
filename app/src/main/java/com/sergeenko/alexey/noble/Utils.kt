package com.sergeenko.alexey.noble

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.activity_new_client.*
import kotlinx.android.synthetic.main.calendar.view.*
import java.io.ByteArrayOutputStream
import java.util.*

fun hideKeyboard(activity: Activity?) {
    val inputManager: InputMethodManager? = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val v = activity?.currentFocus ?: return
    inputManager?.hideSoftInputFromWindow(v.windowToken, 0)
}

fun getAgeFromLong(dateOfBirthLong: Long) = getDiffYears(dateOfBirthLong * 1000, Calendar.getInstance().time.time).toString()

fun convertStringToDate(get: Int) = ("00${get}").substring(get.toString().length)

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

fun showCalendarView(context: Context, view: View, func: (day: Int, month: Int, year: Int)-> Unit) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val calendarView: View = inflater.inflate(R.layout.calendar, null)
    val mPopupWindow = PopupWindow(calendarView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
    mPopupWindow.elevation = 5.0f
    mPopupWindow.isFocusable = true
    mPopupWindow.isOutsideTouchable = true

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