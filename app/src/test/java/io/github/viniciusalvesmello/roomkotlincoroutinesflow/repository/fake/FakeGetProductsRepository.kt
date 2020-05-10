package io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.fake

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductsRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.ResourceResponse

class FakeGetProductsRepository(
    private val productsResourceResponse: ResourceResponse<List<Product>> = ResourceResponse.empty()
) : GetProductsRepository {

    override fun getProducts(): ResourceResponse<List<Product>> = productsResourceResponse

    override fun cancel() { }
}