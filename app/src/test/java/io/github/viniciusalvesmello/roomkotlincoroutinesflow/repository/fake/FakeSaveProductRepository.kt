package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.fake

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.SaveProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.factory.ProductFactory.Factory.makeProduct
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import kotlinx.coroutines.delay

class FakeSaveProductRepository(
    private val product: Product = makeProduct(id = 1),
    private val loadingSaveProduct: Boolean = false,
    private val saveProductSuccess: Boolean = true,
    private val error: Throwable = Throwable()
) : SaveProductRepository {

    override suspend fun saveProduct(
        product: Product,
        onSuccess: (newId: Long?) -> Unit,
        onError: (error: Throwable) -> Unit
    ) {
        if(loadingSaveProduct) {
            delay(2000L)
        }
        if(saveProductSuccess) {
            onSuccess(product.id.toLong())
        } else {
            onError(error)
        }
    }
}