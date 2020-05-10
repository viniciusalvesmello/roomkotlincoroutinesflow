package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TestObserver<T>(target: LiveData<T>) {
    private val values = mutableListOf<T>()
    private val fakeObserver = Observer<T> {
        values += it
    }

    init {
        target.observeForever(fakeObserver)
    }

    fun assertValue(value: T) {
        assert(value == values.last())
    }

    fun assertValue(value: T, numInvocations: Int = 1) {
        assert(numInvocations == values.size)
        assert(value == values.last())
    }

    fun assertNotInvoked() {
        assert(0 == values.size)
    }

    fun assertInvoked(numInvocations: Int = 1) {
        assert(numInvocations == values.size)
    }

    fun inOrder(block: InOrder<T>.() -> Unit) {
        InOrder(values).block()
    }
}