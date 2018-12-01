package com.wonpyohong.android.cleanking.support

import android.arch.lifecycle.MutableLiveData
import android.os.Looper

fun <T> MutableLiveData<T>.notifyObserver() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        this.value = this.value
    } else {
        this.postValue(value)
    }
}