package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test

fun randomString(): String = java.util.UUID.randomUUID().toString()

fun randomLong(): Long = (1L..100L).random()