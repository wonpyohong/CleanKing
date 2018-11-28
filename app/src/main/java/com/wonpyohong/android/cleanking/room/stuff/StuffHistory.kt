package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableBoolean

@Entity(tableName = "stuff_history")
class StuffHistory(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val date: String,                           // 일단 String으로 하자
        val stuffId: Int)