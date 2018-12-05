package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface StuffHistoryDao {
    @Query("SELECT C.id, C.date, C.stuffId, B.stuffName stuffName, A.categoryName categoryName FROM stuff_history C INNER JOIN category A, stuff B ON B.id = C.stuffId AND B.categoryId = A.categoryId")
    fun getAllDumpList(): Flowable<List<StuffHistoryJoin>>

    @Query("SELECT C.id, C.date, C.stuffId, B.stuffName stuffName, A.categoryName categoryName FROM stuff_history C INNER JOIN category A, stuff B ON B.id = C.stuffId AND B.categoryId = A.categoryId WHERE date=:date")
    fun getDayStuffList(date: String): Flowable<List<StuffHistoryJoin>>

    @Query("DELETE FROM stuff_history")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stuffHistory: StuffHistory): Long

    @Update
    fun update(vararg stuffHistory: StuffHistory)

    @Delete
    fun delete(vararg stuffHistory: StuffHistory)
}