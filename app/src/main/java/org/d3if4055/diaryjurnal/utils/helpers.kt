package org.d3if4055.diaryjurnal.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE, dd MMMM yyyy")
        .format(systemTime).toString()
}
