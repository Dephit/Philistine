package com.sergeenko.alexey.noble

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
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
