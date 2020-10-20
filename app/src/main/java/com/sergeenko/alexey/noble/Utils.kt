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
