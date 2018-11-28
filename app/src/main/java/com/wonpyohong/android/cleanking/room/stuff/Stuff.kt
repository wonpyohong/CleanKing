package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.ObservableBoolean

@Entity(tableName = "stuff")
class Stuff(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val categoryId: Int,                           // id로 할 것인가. 사용자가 카테고리를 수정, 삭제 시에 어떻게 할지? foreign키로 연결 해야 하는가?
        val stuffName: String) {

        @Ignore
        var isSelected = ObservableBoolean(false)

        fun onClicked() {
                isSelected.set(!isSelected.get())
        }

        fun setSelectedFalse() {
                isSelected.set(false)
        }
}