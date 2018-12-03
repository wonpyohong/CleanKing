package com.wonpyohong.android.cleanking.ui.add

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseActivity
import com.wonpyohong.android.cleanking.hideKeyboard

class WriteStuffHistoryActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_stuff_history)

        initFragment()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initFragment() {
        replaceFragment(R.id.fragment_container, WriteStuffHistoryFragment())
    }
}