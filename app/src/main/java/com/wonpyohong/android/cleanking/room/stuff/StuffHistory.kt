package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.databinding.ObservableBoolean

@Entity(tableName = "stuff_history", foreignKeys = arrayOf(ForeignKey(entity = Stuff::class,
                                                                parentColumns = arrayOf("id"),
                                                                childColumns = arrayOf("stuffId"),
                                                                onDelete = CASCADE)))
class StuffHistory(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val date: String,                           // 일단 String으로 하자
        val stuffId: Int) {
}

class StuffHistoryJoin(
    @Embedded
    val stuffHistory: StuffHistory,
    var categoryName: String = "a",
    var stuffName: String = "a"
)