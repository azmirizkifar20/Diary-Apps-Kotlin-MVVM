package org.d3if4055.diaryjurnal.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DiaryDao {

    @Insert
    fun insert(diary: Diary)

    @Update
    fun update(diary: Diary)

    @Query("SELECT * FROM diary")
    fun getDiary(): LiveData<List<Diary>>

    @Query("DELETE FROM diary")
    fun clear()

    @Query("DELETE FROM diary WHERE id = :diaryId")
    fun delete(diaryId: Long)

}