package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface StuffHistoryDao {
    @Query("SELECT * FROM stuff_history")
    fun getAllDumpList(): Flowable<List<StuffHistory>>

    @Query("SELECT * FROM stuff_history WHERE date=:date")
    fun getDayStuffList(date: String): Flowable<List<StuffHistory>>

    @Query("DELETE FROM stuff_history")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stuffHistory: StuffHistory): Long

    @Update
    fun update(vararg stuffHistory: StuffHistory)

    @Delete
    fun delete(vararg stuffHistory: StuffHistory)
}