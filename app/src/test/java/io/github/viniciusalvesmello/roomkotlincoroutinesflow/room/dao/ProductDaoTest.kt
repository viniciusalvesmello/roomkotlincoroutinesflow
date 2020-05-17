package io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao

import androidx.room.Room
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.AppDatabase
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory.ProductEntityFactory.Factory.makeProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.FakeAppCoroutinesImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.CountDownLatch

@RunWith(RobolectricTestRunner::class)
class ProductDaoTest {

    private val appDatabase: AppDatabase = Room.inMemoryDatabaseBuilder(
        RuntimeEnvironment.application.baseContext,
        AppDatabase::class.java
    ).build()
    private val productDao: ProductDao = appDatabase.productDao()
    private val appCoroutines: AppCoroutines = FakeAppCoroutinesImpl()

    @Test
    fun `insert products should select all products`() = runBlocking {
        val listProductEntity: List<ProductEntity> = mutableListOf<ProductEntity>().apply {
            add(makeProductEntity())
            add(makeProductEntity())
            add(makeProductEntity())
        }

        val expectedResponse: MutableList<ProductEntity> = mutableListOf()
        val response: MutableList<ProductEntity> = mutableListOf()

        listProductEntity.forEach {
            expectedResponse.add(it.copy(id = productDao.insert(it)?.toInt() ?: 0))
        }

        val latch = CountDownLatch(1)
        productDao.selectProducts()
            .flowOn(appCoroutines.dispatcherIO())
            .onEach {
                response.addAll(it)
                latch.countDown()
            }.launchIn(appCoroutines.scope())
        latch.await()

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `insert products should select all products with delay`() = runBlocking {
        val listProductEntity: List<ProductEntity> = mutableListOf<ProductEntity>().apply {
            add(makeProductEntity())
            add(makeProductEntity())
            add(makeProductEntity())
        }

        val expectedResponse: MutableList<ProductEntity> = mutableListOf()
        val response: MutableList<ProductEntity> = mutableListOf()
        listProductEntity.forEach {
            expectedResponse.add(it.copy(id = productDao.insert(it)?.toInt() ?: 0))
        }

        productDao.selectProducts()
            .flowOn(appCoroutines.dispatcherIO())
            .onEach {
                response.addAll(it)
            }.launchIn(appCoroutines.scope())

        delay(2000L)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `insert product should select product`() = runBlocking {
        val productEntity: ProductEntity = makeProductEntity(1)

        productDao.insert(productEntity)

        val response = productDao.selectProduct(1)

        assertEquals(productEntity, response)
    }
}
