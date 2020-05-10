package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.mapper.toProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory.ProductEntityFactory.Factory.makeProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.fake.FakeProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.FakeAppCoroutinesImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.StateView
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test.test
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetProductsRepositoryTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val appCoroutines: AppCoroutines = FakeAppCoroutinesImpl()

    private lateinit var productDao: ProductDao
    private lateinit var getProductsRepository: GetProductsRepository

    private val listProductEntity = listOf(makeProductEntity())
    private val listProduct = listProductEntity.map { it.toProduct() }

    @Test
    fun `get products should check loading`() {
        productDao = FakeProductDao(selectProductsLoading = true)
        getProductsRepository = GetProductsRepositoryImpl(appCoroutines, productDao)

        getProductsRepository.getProducts().apply {
            state.test().assertValue(StateView.LOADING)
            data.test().assertNotInvoked()
            error.test().assertNotInvoked()
        }
    }

    @Test
    fun `get products should check success`() {
        productDao = FakeProductDao(listProducts = listProductEntity)
        getProductsRepository = GetProductsRepositoryImpl(appCoroutines, productDao)

        getProductsRepository.getProducts().apply {
            state.test().assertValue(StateView.SUCCESS)
            data.test().assertValue(listProduct)
            error.test().assertNotInvoked()
        }
    }

    @Test
    fun `get products should check error`() {
        val throwable = Throwable("Error get products")

        productDao = FakeProductDao(selectProductsSuccess = false, error = throwable)
        getProductsRepository = GetProductsRepositoryImpl(appCoroutines, productDao)

        getProductsRepository.getProducts().apply {
            state.test().assertValue(StateView.ERROR)
            data.test().assertNotInvoked()
            error.test().assertValue(throwable)
        }
    }
}
