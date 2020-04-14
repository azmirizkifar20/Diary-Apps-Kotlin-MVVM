package org.d3if4055.diaryjurnal.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDao

class DiaryViewModel(
    val database: DiaryDao,
    application: Application
) :AndroidViewModel(application) {

    val diary = database.getDiary()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    fun onClickDelete(diaryId: Long) {
        uiScope.launch {
            delete(diaryId)
        }
    }

    private suspend fun delete(diaryId: Long) {
        withContext(Dispatchers.IO) {
            database.delete(diaryId)
        }
    }

    class Factory (
        private val dataSource: DiaryDao,
        private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
                return DiaryViewModel(
                    dataSource,
                    application
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}