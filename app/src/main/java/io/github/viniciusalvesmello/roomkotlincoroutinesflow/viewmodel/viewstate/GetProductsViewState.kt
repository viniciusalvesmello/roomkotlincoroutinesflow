package io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.viewstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product

data class GetProductsViewState(
    var loadingProducts: LiveData<Boolean> = MutableLiveData(),
    var products: LiveData<List<Product>> = MutableLiveData(),
    var getProductsError: LiveData<Throwable> = MutableLiveData()
)