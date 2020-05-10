package io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao

import androidx.room.Room
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.AppDatabase
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.factory.ProductEntityFactory.Factory.makeProductEntity
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.FakeAppCoroutinesImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.launchIO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.CountDownLatch

@RunWith(RobolectricTestRunner::class)
class ProductDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var appCoroutines: AppCoroutines

    @Before
    fun executeBefore() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.application.baseContext,
            AppDatabase::class.java
        ).build()
        productDao = appDatabase.productDao()
        appCoroutines = FakeAppCoroutinesImpl()
    }

    @After
    fun executeAfter() {
        appDatabase.close()
    }

    @Test
    fun `insert products should select all products`() {
        val listProductEntity: List<ProductEntity> = mutableListOf<ProductEntity>().apply {
            add(makeProductEntity())
            add(makeProductEntity())
            add(makeProductEntity())
        }

        val expectedResponse: MutableList<ProductEntity> = mutableListOf()
        val response: MutableList<ProductEntity> = mutableListOf()
        val latch = CountDownLatch(1)
        appCoroutines.launchIO {
            listProductEntity.forEach {
                expectedResponse.add(it.copy(id = productDao.insert(it)?.toInt() ?: 0))
            }

            productDao.selectProducts().collect { list ->
                list.forEach { response.add(it) }
                latch.countDown()
            }
        }
        latch.await()

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
