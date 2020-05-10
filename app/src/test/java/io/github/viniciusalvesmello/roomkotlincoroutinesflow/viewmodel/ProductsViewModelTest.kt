package io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.factory.ProductFactory.Factory.makeProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.fake.FakeGetProductsRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.fake.FakeSaveProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppDispatchers
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.FakeAppDispatchersImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.ResourceResponse
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.StateView
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.buildResourceResponse
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.test.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProductsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getProductRepository: GetProductRepository = mockk()
    private val appDispatchers: AppDispatchers = FakeAppDispatchersImpl()
    private lateinit var productsViewModel: ProductsViewModel

    private val productOne: Product = makeProduct(id = 1)
    private val productTwo: Product = makeProduct(id = 2)
    private val productThree: Product = makeProduct(id = 3)
    private val listProducts = listOf(productOne, productTwo, productThree)
    private val throwable = Throwable("Error products ViewModel")

    @Test
    fun `get products should check loading`() {
        val (data, state, error) = buildResourceResponse<List<Product>>()
        state.value = StateView.LOADING

        val productsResourceResponse = ResourceResponse<List<Product>>(state, data, error)

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(productsResourceResponse),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(),
            appDispatchers = appDispatchers
        )

        productsViewModel.getProducts()

        productsViewModel.getProductsViewState.apply {
            loadingProducts.test().assertValue(true)
            products.test().assertNotInvoked()
            getProductsError.test().assertNotInvoked()
        }
    }

    @Test
    fun `get products should check success`() {
        val (data, state, error) = buildResourceResponse<List<Product>>()
        state.value = StateView.SUCCESS
        data.value = listProducts

        val productsResourceResponse = ResourceResponse<List<Product>>(state, data, error)

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(productsResourceResponse),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(),
            appDispatchers = appDispatchers
        )

        productsViewModel.getProducts()

        productsViewModel.getProductsViewState.apply {
            loadingProducts.test().assertValue(false)
            products.test().assertValue(listProducts)
            getProductsError.test().assertNotInvoked()
        }
    }

    @Test
    fun `get products should check error`() {
        val (data, state, error) = buildResourceResponse<List<Product>>()
        state.value = StateView.ERROR
        error.value = throwable

        val productsResourceResponse = ResourceResponse<List<Product>>(state, data, error)

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(productsResourceResponse),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(),
            appDispatchers = appDispatchers
        )

        productsViewModel.getProducts()

        productsViewModel.getProductsViewState.apply {
            loadingProducts.test().assertValue(false)
            products.test().assertNotInvoked()
            getProductsError.test().assertValue(throwable)
        }
    }

    @Test
    fun `get product should check loading`() {

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(),
            appDispatchers = appDispatchers
        )

        coEvery {
            getProductRepository.getProduct(1)
        } returns flow {
            emit(productOne)
        }.onStart {
            delay(2000L)
        }

        productsViewModel.getProduct(1)

        productsViewModel.getProductViewState.apply {
            loadingProduct.test().assertValue(true)
            product.test().assertNotInvoked()
            getProductError.test().assertNotInvoked()
        }
    }

    @Test
    fun `get product should check success`() {

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(),
            appDispatchers = appDispatchers
        )

        coEvery {
            getProductRepository.getProduct(1)
        } returns flow {
            emit(productOne)
        }

        productsViewModel.getProduct(1)

        productsViewModel.getProductViewState.apply {
            loadingProduct.test().assertValue(false)
            product.test().assertValue(productOne)
            getProductError.test().assertNotInvoked()
        }
    }

    @Test
    fun `get product should check error`() {

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(),
            appDispatchers = appDispatchers
        )

        coEvery {
            getProductRepository.getProduct(1)
        } returns flow {
            throw throwable
            emit(productOne)
        }

        productsViewModel.getProduct(1)

        productsViewModel.getProductViewState.apply {
            loadingProduct.test().assertValue(false)
            product.test().assertNotInvoked()
            getProductError.test().assertValue(throwable)
        }
    }

    @Test
    fun `save product should check loading`() {

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(
                product = productOne,
                loadingSaveProduct = true
            ),
            appDispatchers = appDispatchers
        )

        productsViewModel.loadingSaveProduct = MutableLiveData()
        productsViewModel.saveProduct = MutableLiveData()

        productsViewModel.saveProduct(productOne)

        productsViewModel.loadingSaveProduct.test().assertValue(true)
        productsViewModel.saveProduct.test().assertNotInvoked()
    }

    @Test
    fun `save product should check success`() {

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(product = productOne),
            appDispatchers = appDispatchers
        )

        productsViewModel.loadingSaveProduct = MutableLiveData()
        productsViewModel.saveProduct = MutableLiveData()

        productsViewModel.saveProduct(productOne)

        productsViewModel.loadingSaveProduct.test().assertValue(true)
        productsViewModel.saveProduct.test().assertValue(true)
    }

    @Test
    fun `save product should check error`() {

        productsViewModel = ProductsViewModel(
            getProductsRepository = FakeGetProductsRepository(),
            getProductRepository = getProductRepository,
            saveProductRepository = FakeSaveProductRepository(
                product = productOne,
                saveProductSuccess = false,
                error = throwable
            ),
            appDispatchers = appDispatchers
        )

        productsViewModel.loadingSaveProduct = MutableLiveData()
        productsViewModel.saveProduct = MutableLiveData()

        productsViewModel.saveProduct(productOne)

        productsViewModel.loadingSaveProduct.test().assertValue(false)
        productsViewModel.saveProduct.test().assertValue(false)
    }
}