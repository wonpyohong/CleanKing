package com.wonpyohong.android.cleanking.ui

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import com.wonpyohong.android.cleanking.R


class FabBehavior(context: Context, attrs: AttributeSet): CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        val scaleFactor = -0.001 * dependency!!.y + 1
        val scaleFactorAdjusted = if (scaleFactor < 0) 0f else scaleFactor.toFloat()

        val fab = child!!.findViewById<FloatingActionButton>(R.id.fab)
        fab.scaleX = scaleFactorAdjusted
        fab.scaleY = scaleFactorAdjusted
        return false
    }
}