package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper.toProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface GetProductRepository {
    fun getProduct(id: Int): Flow<Product>
}

class GetProductRepositoryImpl(
    private val productDao: ProductDao
) : GetProductRepository {

    override fun getProduct(id: Int): Flow<Product> = flow {
        emit(productDao.selectProduct(id).toProduct())
    }
}