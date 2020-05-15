package io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductsRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.SaveProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppDispatchers
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.ResourceResponse
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.StateView
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.asMutable
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.viewstate.GetProductViewState
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.viewstate.GetProductsViewState
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductsRepository: GetProductsRepository,
    private val getProductRepository: GetProductRepository,
    private val saveProductRepository: SaveProductRepository,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val getProducts: MutableLiveData<ResourceResponse<List<Product>>> = MutableLiveData()

    val getProductsViewState = GetProductsViewState().apply {
        loadingProducts = map(switchMap(getProducts) { it.state }) {
            it == StateView.LOADING
        }
        products = switchMap(getProducts) { it.data }
        getProductsError = switchMap(getProducts) { it.error }
    }

    lateinit var getProductViewState: GetProductViewState
    lateinit var loadingSaveProduct: LiveData<Boolean>
    lateinit var saveProduct: LiveData<Boolean>

    fun getProducts() {
        getProducts.postValue(getProductsRepository.getProducts())
    }

    private fun initProductLiveData() {
        getProductViewState = GetProductViewState()
        loadingSaveProduct = MutableLiveData()
        saveProduct = MutableLiveData()
    }

    fun getProduct(id: Int) {
        initProductLiveData()

        if (id == 0) {
            return
        }

        viewModelScope.launch(appDispatchers.dispatcherIO()) {
            getProductRepository.getProduct(id)
                .buffer()
                .onStart {
                    getProductViewState.loadingProduct.asMutable.postValue(true)
                }.onEach {
                    getProductViewState.loadingProduct.asMutable.postValue(false)
                    getProductViewState.product.asMutable.postValue(it)
                }.catch {
                    getProductViewState.loadingProduct.asMutable.postValue(false)
                    getProductViewState.getProductError.asMutable.postValue(it)
                }.collect()
        }
    }

    fun saveProduct(product: Product) {
        loadingSaveProduct.asMutable.postValue(true)
        viewModelScope.launch(appDispatchers.dispatcherIO()) {
            saveProductRepository.saveProduct(product = product, onSuccess = {
                saveProduct.asMutable.postValue(true)
            }, onError = {
                saveProduct.asMutable.postValue(false)
                loadingSaveProduct.asMutable.postValue(false)
            })
        }
    }

    override fun onCleared() {
        getProductsRepository.cancel()

        super.onCleared()
    }
}