package io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.fake

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory.ProductEntityFactory.Factory.makeProductEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class FakeProductDao(
    private val selectProductsLoading: Boolean = false,
    private val selectProductsSuccess: Boolean = true,
    private val listProducts: List<ProductEntity> = listOf(),
    private val selectProductSuccess: Boolean = true,
    private val product: ProductEntity = makeProductEntity(),
    private val insertSuccess: Boolean = true,
    private val error: Throwable = Throwable()
) : ProductDao {

    override fun selectProducts(): Flow<List<ProductEntity>> = flow {
        if (selectProductsSuccess) {
            emit(listProducts)
        } else {
            throw error
        }
    }.onStart {
        if (selectProductsLoading) {
            delay(2000L)
        }
    }

    override suspend fun selectProduct(id: Int): ProductEntity {
        if (!selectProductSuccess) {
            throw error
        }

        return product
    }

    override suspend fun insert(productEntity: ProductEntity): Long? {
        if (!insertSuccess) {
            throw error
        }

        return product.id.toLong()
    }
}