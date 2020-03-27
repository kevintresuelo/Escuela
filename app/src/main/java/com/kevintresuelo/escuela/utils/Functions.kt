package com.kevintresuelo.escuela.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.view.inputmethod.InputMethodManager
import com.kevintresuelo.escuela.R
import org.ocpsoft.prettytime.PrettyTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.isBlankOrEmpty(): Boolean {
    return this.isBlank() || this.isEmpty()
}

fun formatDate(date: Date): String {
    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
}

fun formatDateRange(context: Context, startDate: Date?, endDate: Date?): String? {
    if (startDate == null || endDate == null) {
        return null
    }
    val start = formatDate(startDate)
    val end = formatDate(endDate)
    return context.resources.getString(R.string.academicterm_daterange_format, start, end)
}

fun formatDateRange(context: Context, startDate: Long?, endDate: Long?): String? {
    if (startDate == null || endDate == null) {
        return null
    }
    return formatDateRange(context, Date(startDate), Date(endDate))
}

fun getLocale(context: Context): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        context.resources.configuration.locales.get(0)
    } else{
        context.resources.configuration.locale
    }
}

fun formatTimeAgo(context: Context, date: Date, threshold: Long = DateUtils.DAY_IN_MILLIS, thresholdPattern: Int? = null): String {
    return if (threshold > 0L && Date().time - date.time > threshold) {
        if (thresholdPattern != null) {
            context.getString(thresholdPattern, formatDate(date))
        } else {
            formatDate(date)
        }
    } else {
        PrettyTime(Date()).setLocale(getLocale(context)).format(date)
    }
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}