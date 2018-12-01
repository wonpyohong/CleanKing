package com.wonpyohong.android.cleanking.base

import android.arch.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    override fun onCleared() {
        super.onCleared()
    }
}