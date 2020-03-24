package org.d3if4055.diaryjurnal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDao
import org.d3if4055.diaryjurnal.recyclerview.DiaryAdapter
import org.d3if4055.diaryjurnal.utils.formatDiary

class DiaryViewModel(
    val database: DiaryDao,
    application: Application
) :AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val diary = database.getDiary()

    fun onClickInsert(message: String) {
        uiScope.launch {
            val diary = Diary(0, message)
            insert(diary)
        }
    }

    private suspend fun insert(diary: Diary) {
        withContext(Dispatchers.IO) {
            database.insert(diary)
        }
    }

    fun onClickClear() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

}