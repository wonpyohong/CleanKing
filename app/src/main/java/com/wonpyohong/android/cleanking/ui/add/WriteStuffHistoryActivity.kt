package com.wonpyohong.android.cleanking.ui.add

import android.os.Bundle
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseActivity
import com.wonpyohong.android.cleanking.ui.CalendarFragment

class WriteStuffHistoryActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dump)

        initFragment()
    }

    private fun initFragment() {
        replaceFragment(R.id.fragment_container, AddDumpFragment())
    }
}