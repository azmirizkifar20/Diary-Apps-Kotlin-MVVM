package org.d3if4055.diaryjurnal.utils

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.Diary
import java.lang.StringBuilder
import java.text.SimpleDateFormat

fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE, dd MMMM yyyy")
        .format(systemTime).toString()
}

// ga kepake
fun formatDiary(diary:List<Diary>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply{
        append(resources.getString(R.string.title))
        diary.forEach{
            append("<br>")
            append("<b>${convertLongToDateString(it.tanggal)}</b>")
            append("<br>")
            append("${it.message}")
            append("<br>")
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}