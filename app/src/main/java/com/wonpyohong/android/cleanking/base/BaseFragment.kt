package com.wonpyohong.android.cleanking.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: Fragment() {
    protected abstract var fragmentLayoutId: Int

    protected val createDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(fragmentLayoutId, container, false)
    }

    protected fun replaceFragment(containerId: Int, fragment: BaseFragment) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        createDisposable.dispose()

        super.onDestroy()
    }
}