package com.wonpyohong.android.cleanking

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun showKeyboard(editText: EditText) {
    val inputMethodManager = CleanApplication.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(editText, 0)
}

fun hideKeyboard(editText: EditText) {
    val inputMethodManager = CleanApplication.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0)
}