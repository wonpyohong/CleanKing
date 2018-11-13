package com.wonpyohong.android.cleanking.base

import android.support.v7.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {
    protected fun replaceFragment(fragmentId: Int, fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.commit()
    }
}