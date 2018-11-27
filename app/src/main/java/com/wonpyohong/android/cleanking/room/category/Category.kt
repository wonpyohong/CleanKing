package com.wonpyohong.android.cleanking.room.category

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "category")
class Category(
        @PrimaryKey(autoGenerate = true)
        val categoryId: Int,
        val categoryName: String)