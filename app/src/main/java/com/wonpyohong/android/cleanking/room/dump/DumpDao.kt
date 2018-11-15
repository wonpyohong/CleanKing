package com.homedev.android.dietapp.room.exercise

import android.arch.persistence.room.*
import com.wonpyohong.android.cleanking.room.dump.Dump
import io.reactivex.Flowable

@Dao
interface DumpDao {
    @Query("SELECT * FROM dump")
    fun getAllDumpList(): Flowable<List<Dump>>

    @Query("SELECT * FROM dump WHERE date=:date")
    fun getDumpList(date: String): Flowable<List<Dump>>

    @Query("DELETE FROM dump")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dump: Dump): Long

    @Update
    fun update(vararg dump: Dump)

    @Delete
    fun delete(vararg dump: Dump)
}