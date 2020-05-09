package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocus: View? = currentFocus

    if (currentFocus != null) {
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Activity.hideKeyboard(hideImplicitOnly: Boolean = false) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocus: View? = currentFocus

    if (currentFocus != null) {
        currentFocus.clearFocus()
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    if(hideImplicitOnly) {
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}