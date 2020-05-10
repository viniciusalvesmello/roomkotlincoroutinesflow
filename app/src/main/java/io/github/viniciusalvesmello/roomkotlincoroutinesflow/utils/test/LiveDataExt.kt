package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.test() = TestObserver(this)