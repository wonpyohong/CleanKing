package com.wonpyohong.android.cleanking.room.stuff

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategoryList(): LiveData<List<Category>>

    @Query("DELETE FROM category")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Long

    @Update
    fun update(vararg category: Category)

    @Delete
    fun delete(vararg category: Category)
}