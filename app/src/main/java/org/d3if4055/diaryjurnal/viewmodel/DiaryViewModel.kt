package org.d3if4055.diaryjurnal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDao

class DiaryViewModel(
    val database: DiaryDao,
    application: Application
) :AndroidViewModel(application) {

    // buat viewModelJob dan UiScope
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    // buat variabel diary dan isi dengan data diary
    val diary = database.getDiary()

    // fungsi insert untuk dipanggil di fragment tambah diary
    fun onClickInsert(message: String) {
        uiScope.launch {
            val diary = Diary(0, message)
            // panggil fungsi insert yang di buat dibawah fungsi onClickInsert
            insert(diary)
        }
    }

    // buat suspend fun insert untuk insert data ke roomDB
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

    fun onClickUpdate(diary: Diary) {
        uiScope.launch {
            update(diary)
        }
    }

    private suspend fun update(diary: Diary) {
        withContext(Dispatchers.IO) {
            database.update(diary)
        }
    }

}