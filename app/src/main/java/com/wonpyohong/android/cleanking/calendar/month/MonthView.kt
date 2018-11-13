package com.wonpyohong.android.cleanking.calendar.month

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.WeekFields
import java.util.*


class MonthView(context: Context, attrs: AttributeSet): View(context, attrs) {
    val NUM_WEEK_DAY = 7

    private val dayList = mutableListOf<DayOfMonth>()

    val gestureDetector = GestureDetector(context, MonthViewGestureListener(dayList, this))

    private var weekNum = 5
    var firstDateInGrid = LocalDate.now()
    var firstDate = LocalDate.now()
        set(firstDate) {
            firstDateInGrid = firstDate.with(WeekFields.of(Locale.US).dayOfWeek(), 1)
            weekNum = getWeekNum(firstDate)

            dayList.clear()
            for(offset in 1..(NUM_WEEK_DAY * weekNum)) {
                dayList.add(DayOfMonth(context, firstDate, firstDateInGrid.plusDays(offset.toLong() - 1)))
            }

            field = firstDate
        }

    private fun getWeekNum(firstDate: LocalDate): Int {
        val lastDateInGrid = firstDate.withDayOfMonth(firstDate.lengthOfMonth()).with(WeekFields.of(Locale.US).dayOfWeek(), 7)
        return (ChronoUnit.DAYS.between(firstDateInGrid, lastDateInGrid).toInt() + 1) / NUM_WEEK_DAY
    }

    override fun onTouchEvent(event: MotionEvent?) = gestureDetector.onTouchEvent(event)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        dayList.forEachIndexed { index, dayOfMonth ->
            dayOfMonth.xStart = width.toFloat() / NUM_WEEK_DAY * (index % NUM_WEEK_DAY)
            dayOfMonth.yStart = height.toFloat() / weekNum * (index / NUM_WEEK_DAY) + 50
            dayOfMonth.width = width.toFloat() / NUM_WEEK_DAY
            dayOfMonth.height = height.toFloat() / weekNum

            dayOfMonth.draw(canvas)
        }
    }
}