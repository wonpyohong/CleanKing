package com.wonpyohong.android.cleanking.calendar.day

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.calendar.month.MIN_DATE
import com.wonpyohong.android.cleanking.ui.selectedDate

class DayRecyclerView(context: Context, attrs: AttributeSet): RecyclerView(context, attrs) {
    fun canItemScrollVertically(direction: Int): Boolean {
        val currentPosition = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val currentDayView = layoutManager.findViewByPosition(currentPosition)
        val exerciseTodoRecyclerView = currentDayView.findViewById<RecyclerView>(R.id.dumpRecyclerView)
        return exerciseTodoRecyclerView.canScrollVertically(direction)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            val currentPosition = (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            selectedDate = MIN_DATE.plusDays(currentPosition.toLong())
        }
    }
}