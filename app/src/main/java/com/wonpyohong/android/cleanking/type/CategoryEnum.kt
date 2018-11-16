package com.wonpyohong.android.cleanking.type

import android.graphics.Color
import com.cunoraz.tagview.Tag

enum class CategoryEnum(val title: String, val color: Int) {
    DISCARD("버리기", Color.RED),
    MOVE("자리 이동", Color.BLUE),
    HOUSE_WORK("집안일", Color.LTGRAY),
    TRASH("쓰레기", Color.LTGRAY),
    CLEAN("청소", Color.MAGENTA),
    ORGANIZE("정리", Color.MAGENTA)
    ;

    companion object {
        fun createTagList() =
            values().map {
                val tag = Tag(it.title)
                with (tag) {
                    tagTextColor = it.color
                    layoutBorderColor = it.color
                    layoutColorPress = it.color
                    layoutBorderSize = 1f
                    layoutColor = Color.WHITE
                    tagTextSize = 20f
                }

                tag
            }
    }
}