package io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test.randomLong
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test.randomString

class ProductEntityFactory {
    companion object Factory {

        fun makeProductEntity(id: Int = 0) = ProductEntity(
            id = id,
            description = randomString(),
            value = randomLong().toDouble()
        )
    }
}