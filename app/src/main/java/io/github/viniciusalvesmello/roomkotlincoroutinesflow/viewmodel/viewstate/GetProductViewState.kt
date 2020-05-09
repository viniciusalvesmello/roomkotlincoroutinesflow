package io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.viewstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product

data class GetProductViewState(
    var loadingProduct: LiveData<Boolean> = MutableLiveData(),
    var product: LiveData<Product> = MutableLiveData(),
    var getProductError: LiveData<Throwable> = MutableLiveData()
)