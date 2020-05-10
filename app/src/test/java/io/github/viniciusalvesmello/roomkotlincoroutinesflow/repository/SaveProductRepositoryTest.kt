package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper.toProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory.ProductEntityFactory
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.fake.FakeProductDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SaveProductRepositoryTest {

    private lateinit var productDao: ProductDao
    private lateinit var saveProductRepository: SaveProductRepository

    private val productEntity = ProductEntityFactory.makeProductEntity(id = 1)
    private val product = productEntity.toProduct()


    @Test
    fun `insert product should check success`() = runBlocking {
        productDao = FakeProductDao(product = productEntity)
        saveProductRepository = SaveProductRepositoryImpl(productDao)

        var executeOnSuccess = false
        saveProductRepository.saveProduct(product = product, onSuccess = {
            executeOnSuccess = true
        }, onError = {
            executeOnSuccess = false
        })

        assertEquals(true, executeOnSuccess)
    }

    @Test
    fun `insert product should check error`() = runBlocking {
        val throwable = Throwable("Error insert product on dao")

        productDao = FakeProductDao(product = productEntity, insertSuccess = false, error = throwable)
        saveProductRepository = SaveProductRepositoryImpl(productDao)

        var executeOnError = false
        var throwableOnError: Throwable? = null
        saveProductRepository.saveProduct(product = product, onSuccess = {
            executeOnError = false
        }, onError = {
            executeOnError = true
            throwableOnError = it
        })

        assertEquals(true, executeOnError)
        assertEquals(throwable, throwableOnError)
    }
}