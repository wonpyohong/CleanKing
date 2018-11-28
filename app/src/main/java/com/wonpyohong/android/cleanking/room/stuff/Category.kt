package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableBoolean

@Entity(tableName = "category")
class Category(
        @PrimaryKey(autoGenerate = true)
        val categoryId: Int,
        val categoryName: String) {

    @Ignore
    var isSelected = ObservableBoolean(false)

    fun onClicked() {
        isSelected.set(!isSelected.get())
    }

    fun setSelectedFalse() {
        isSelected.set(false)
    }
}