package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity

fun ProductEntity.toProduct() = Product(
    id = id,
    description = description,
    value = value
)

fun Product.toProductEntity() = ProductEntity(
    id = id,
    description = description,
    value = value
)