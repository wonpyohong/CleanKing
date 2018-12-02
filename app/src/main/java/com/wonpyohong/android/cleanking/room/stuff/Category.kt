package com.wonpyohong.android.cleanking.room.stuff

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableBoolean

@Entity(tableName = "category", indices = [Index(value = ["categoryName"], unique = true)])
class Category(
        @PrimaryKey(autoGenerate = true)
        val categoryId: Int,
        val categoryName: String) {

    @Ignore
    var isSelected = MutableLiveData<Boolean>()

    fun onClicked() {
        isSelected.value = !isSelected.value!!
    }

    fun setSelectedFalse() {
        isSelected.value = false
    }
}