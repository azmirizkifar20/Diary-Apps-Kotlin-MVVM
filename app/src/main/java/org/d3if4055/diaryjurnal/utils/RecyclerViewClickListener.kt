package org.d3if4055.diaryjurnal.utils

import android.view.View
import org.d3if4055.diaryjurnal.database.Diary

interface RecyclerViewClickListener {
    fun onItemClicked(view: View, diary: Diary)
}