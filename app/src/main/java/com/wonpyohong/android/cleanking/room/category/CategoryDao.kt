package com.homedev.android.dietapp.room.exercise

import android.arch.persistence.room.*
import com.wonpyohong.android.cleanking.room.category.Category
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategoryList(): Flowable<List<Category>>

    @Query("DELETE FROM category")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Long

    @Update
    fun update(vararg category: Category)

    @Delete
    fun delete(vararg category: Category)
}