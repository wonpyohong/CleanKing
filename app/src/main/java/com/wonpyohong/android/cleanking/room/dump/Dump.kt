package com.wonpyohong.android.cleanking.room.dump

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "dump")
class Dump(
        @PrimaryKey(autoGenerate = true)
        val dumpId: Int,
        val date: String,                           // 일단 String으로 하자
        val category: String,                       // 날짜에 따른 순서
        val dumpName: String,
        val reason: String)