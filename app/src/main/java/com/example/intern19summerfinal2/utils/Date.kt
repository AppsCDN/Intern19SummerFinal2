package com.example.intern19summerfinal2.utils

import com.example.intern19summerfinal2.utils.FormatDate.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

object FormatDate {
    val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    const val MILLIS_IN_SECOND = 1000
}

fun Long.formatDate(): String = DATE_FORMAT.format(Date(FormatDate.MILLIS_IN_SECOND * this))