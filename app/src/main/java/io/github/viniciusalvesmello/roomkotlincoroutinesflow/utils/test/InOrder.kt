package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test

class InOrder<T>(private val values: List<T>) {
    private var position = 0

    fun verify(value: T) {
        val expected = values[position++]
        assert(expected == value)
    }
}