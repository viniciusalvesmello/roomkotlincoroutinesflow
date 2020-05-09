package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils

import androidx.lifecycle.LiveData

class ResourceResponse<T>(
    val state: LiveData<StateView>,
    val data: LiveData<T>,
    val error: LiveData<Throwable>,
    val retry: (() -> ResourceResponse<T>)? = null
)