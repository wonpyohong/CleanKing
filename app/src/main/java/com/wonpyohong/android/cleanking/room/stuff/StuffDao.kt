package com.wonpyohong.android.cleanking.room.stuff

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface StuffDao {
    @Query("SELECT * FROM stuff")
    fun getStuffList(): Flowable<List<Stuff>>

    @Query("SELECT * FROM stuff WHERE categoryId=:categoryId")
    fun getStuffList(categoryId: Int): LiveData<MutableList<Stuff>>

    @Query("DELETE FROM stuff")
    fun clearAll()

    @Query("UPDATE stuff_history SET stuffId=:newStuffId WHERE stuffId=:oldStuffId")
    fun changeStuffId(oldStuffId: Int, newStuffId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stuff: Stuff): Long

    @Update
    fun update(vararg stuff: Stuff)

    @Delete
    fun delete(vararg stuff: Stuff)
}