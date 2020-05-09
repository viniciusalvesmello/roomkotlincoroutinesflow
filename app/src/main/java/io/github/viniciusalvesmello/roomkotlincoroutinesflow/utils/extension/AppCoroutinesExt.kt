package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import kotlinx.coroutines.launch

fun AppCoroutines.launchIO(block: suspend () -> Unit): Throwable? {
    var error: Throwable? = null
    this.scope().launch(this.dispatcherIO()) {
        try {
            block()
        } catch (t: Throwable) {
            error = t
        }
    }
    return error
}