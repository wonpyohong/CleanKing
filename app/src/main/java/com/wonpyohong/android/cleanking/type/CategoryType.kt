package com.wonpyohong.android.cleanking.type

import android.graphics.Color

enum class CategoryType(val title: String, val color: Int) {
    DISCARD("버리기", Color.RED),
    MOVE("자리 이동", Color.BLUE),
    HOUSE_WORK("집안일", Color.LTGRAY),
    TRASH("쓰레기", Color.LTGRAY),
    CLEAN("청소", Color.MAGENTA),
    ORGANIZE("정리", Color.MAGENTA)
    ;

}