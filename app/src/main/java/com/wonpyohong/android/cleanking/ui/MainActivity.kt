package com.wonpyohong.android.cleanking.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
    }

    private fun initFragment() {
        replaceFragment(R.id.fragment_container, CalendarFragment())
    }
}
