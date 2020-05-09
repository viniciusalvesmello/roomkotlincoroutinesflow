package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper.toProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.execute

interface SaveProductRepository {
    suspend fun saveProduct(
        product: Product,
        onSuccess: (newId: Long?) -> Unit,
        onError: (error: Throwable) -> Unit
    )
}

class SaveProductRepositoryImpl(
    private val productDao: ProductDao
) : SaveProductRepository {

    override suspend fun saveProduct(
        product: Product,
        onSuccess: (newId: Long?) -> Unit,
        onError: (error: Throwable) -> Unit
    ) {
        execute {
            onSuccess(productDao.insert(product.toProductEntity()))
        }?.apply { onError(this) }
    }
}