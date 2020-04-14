package org.d3if4055.diaryjurnal.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "diary")
data class Diary (
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,

    @ColumnInfo(name = "message")
    var message:String,

    @ColumnInfo(name = "tanggal")
    val tanggal:Long = System.currentTimeMillis()
) : Parcelable