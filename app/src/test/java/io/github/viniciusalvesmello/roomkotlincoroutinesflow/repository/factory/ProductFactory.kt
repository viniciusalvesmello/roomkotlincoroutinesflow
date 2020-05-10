package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.factory

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test.randomLong
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test.randomString

class ProductFactory {
    companion object Factory {
        fun makeProduct(id: Int = 0) = Product(
            id = id,
            description = randomString(),
            value = randomLong().toDouble()
        )
    }
}