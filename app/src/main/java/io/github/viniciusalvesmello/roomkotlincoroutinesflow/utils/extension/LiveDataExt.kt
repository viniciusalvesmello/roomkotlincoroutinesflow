package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

val <T> LiveData<T>.asMutable: MutableLiveData<T>
    get() = this as? MutableLiveData<T>
        ?: throw IllegalStateException("$this isn't a valid MutableLiveData")