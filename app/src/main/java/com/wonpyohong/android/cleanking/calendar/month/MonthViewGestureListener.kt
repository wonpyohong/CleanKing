package com.wonpyohong.android.cleanking.calendar.month

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.widget.RelativeLayout
import com.wonpyohong.android.cleanking.ui.selectedDate

class MonthViewGestureListener(val dayList: List<DayOfMonth>, val monthView: MonthView): GestureDetector.SimpleOnGestureListener() {
    override fun onSingleTapUp(event: MotionEvent): Boolean {
        selectedDate = findDay(event).date
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        val imageView = findDay(event).createViewForDragging()

        val relativeLayout = getRelativeLayout()
        relativeLayout.addView(imageView)

        imageView.visibility = View.INVISIBLE

        android.os.Handler().post {
            imageView.startDragAndDrop(null, View.DragShadowBuilder(imageView), imageView, 0)
        }
    }

    private fun findDay(event: MotionEvent): DayOfMonth {
        return dayList.find {
            with(it) {
                xStart < event.x && event.x < xStart + width
                        && yStart < event.y && event.y < yStart + height
            }
        }!!
    }

    private fun getRelativeLayout(): RelativeLayout {
        var tempParent: ViewParent = monthView.parent
        while (tempParent !is RelativeLayout) {
            tempParent = tempParent.parent
        }
        return tempParent
    }
}