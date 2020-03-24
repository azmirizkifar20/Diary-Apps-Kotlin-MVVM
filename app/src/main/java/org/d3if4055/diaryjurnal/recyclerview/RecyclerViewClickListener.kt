package org.d3if4055.diaryjurnal.recyclerview

import android.view.View
import org.d3if4055.diaryjurnal.database.Diary

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, diary: Diary)
}