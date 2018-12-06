package com.wonpyohong.android.cleanking.calendar.day

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlin.math.abs

class DayBehavior(context: Context, attrs: AttributeSet): BottomSheetBehavior<ViewGroup>(context, attrs) {
    private var downX = 0f
    private var downY = 0f
    private var downView: View? = null

    override fun onInterceptTouchEvent(parent: CoordinatorLayout?, child: ViewGroup, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y

                downView = child.getViewByCoordinates(downX, downY)

                super.onInterceptTouchEvent(parent, child, event)
            }
            MotionEvent.ACTION_MOVE -> {
                val isHorizontalScroll = abs(downX - event.x) > abs(downY - event.y)
                if (isHorizontalScroll) {
                    return false
                }

                val isUpward = downY - event.y < -20
                val isDownward = downY - event.y > 20

                if (state == STATE_COLLAPSED || downView !is DayRecyclerView) {
                    true
                } else if (downView is DayRecyclerView) {
                    (isUpward && !(downView as DayRecyclerView).canItemScrollVertically(-1))
                            || (isDownward && !(downView as DayRecyclerView).canItemScrollVertically(1))
                } else {
                    false
                }
            }
            else -> {
                super.onInterceptTouchEvent(parent, child, event)
            }
        }
    }

    fun ViewGroup.getViewByCoordinates(x: Float, y: Float) : View? {
        (0 until this.childCount)
                .map { this.getChildAt(it) }
                .forEach {
                    val bounds = Rect()
                    it.getHitRect(bounds)
                    if (bounds.contains(x.toInt(), y.toInt())) {
                        return it
                    }
                }
        return null
    }
}