package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper.toProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.ResourceResponse
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.asCacheResourceResponse
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface GetProductsRepository {
    fun getProducts(): ResourceResponse<List<Product>>
    fun cancel()
}

class GetProductsRepositoryImpl(
    private val appCoroutines: AppCoroutines,
    private val productDao: ProductDao
) : GetProductsRepository {

    override fun getProducts(): ResourceResponse<List<Product>> =
        productDao.selectProducts()
            .flowOn(appCoroutines.dispatcherIO())
            .map { list -> list.map { it.toProduct() } }
            .asCacheResourceResponse(appCoroutines.scope())

    override fun cancel() {
        appCoroutines.cancel()
    }
}