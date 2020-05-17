package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils

import androidx.lifecycle.LiveData

class ResourceResponse<T>(
    val state: LiveData<StateView>,
    val data: LiveData<T>,
    val error: LiveData<Throwable>
) {
    companion object {
        fun <T> empty(): ResourceResponse<T> {
            val (data, state, error) = buildResourceResponse<T>()
            return ResourceResponse<T>(state, data, error)
        }
    }
}