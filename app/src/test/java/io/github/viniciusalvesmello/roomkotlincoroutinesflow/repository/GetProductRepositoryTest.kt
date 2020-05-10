package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper.toProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory.ProductEntityFactory.Factory.makeProductEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetProductRepositoryTest {

    private val productDao: ProductDao = mockk()
    private val getProductRepository: GetProductRepository = GetProductRepositoryImpl(productDao)

    private val productEntity = makeProductEntity(id = 1)
    private val product = productEntity.toProduct()

    @Test
    fun `get product should check success`() = runBlocking {
        coEvery {
            productDao.selectProduct(id = 1)
        } returns productEntity

        var response: Product? = null
        getProductRepository.getProduct(id = 1).collect {
            response = it
        }

        assertEquals(product, response)
    }

    @Test
    fun `get product should check error`() = runBlocking {
        val throwable = Throwable("Error get product")

        coEvery {
            productDao.selectProduct(id = 1)
        } throws throwable

        var error: Throwable? = null
        try {
            getProductRepository.getProduct(id = 1).collect()
        } catch (t: Throwable) {
            error = t
        }

        assertEquals(throwable, error)
    }
}