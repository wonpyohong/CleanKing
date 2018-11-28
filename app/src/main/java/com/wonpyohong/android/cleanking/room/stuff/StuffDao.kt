package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface StuffDao {
    @Query("SELECT * FROM stuff")
    fun getStuffList(): Flowable<List<Stuff>>

    @Query("SELECT * FROM stuff WHERE categoryId=:categoryId")
    fun getStuffList(categoryId: Int): Flowable<List<Stuff>>

    @Query("DELETE FROM stuff")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stuff: Stuff): Long

    @Update
    fun update(vararg stuff: Stuff)

    @Delete
    fun delete(vararg stuff: Stuff)
}