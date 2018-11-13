package com.wonpyohong.android.cleanking.calendar.month

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MonthBehavior(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        val params = child!!.layoutParams
        params.height = dependency!!.y.toInt()
        child.layoutParams = params
        child.y = 0F

        return true
    }
}