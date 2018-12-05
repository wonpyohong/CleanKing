package com.wonpyohong.android.cleanking.room.stuff

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import android.databinding.ObservableBoolean

@Entity(tableName = "stuff", indices = [Index(value = ["categoryId", "stuffName"], unique = true)],
        foreignKeys = arrayOf(
                ForeignKey(entity = Category::class,
                        parentColumns = arrayOf("categoryId"),
                        childColumns = arrayOf("categoryId"),
                        onDelete = ForeignKey.CASCADE
                )
        ))
class Stuff(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val categoryId: Int,                           // id로 할 것인가. 사용자가 카테고리를 수정, 삭제 시에 어떻게 할지? foreign키로 연결 해야 하는가?
        val stuffName: String,
        var frequency: Int) {

        @Ignore
        var isSelected = MutableLiveData<Boolean>()

        fun onClicked() {
                isSelected.value = !isSelected.value!!
        }

        fun setSelectedTrue() {
                isSelected.value = true
        }

        fun setSelectedFalse() {
                isSelected.value = false
        }
}