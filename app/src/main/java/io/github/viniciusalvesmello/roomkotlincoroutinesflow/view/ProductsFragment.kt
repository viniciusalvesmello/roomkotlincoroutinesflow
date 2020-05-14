package io.github.viniciusalvesmello.roomkotlincoroutinesflow.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.R
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.gone
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.observe
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.visible
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.showMessage
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val productsViewModel by sharedViewModel<ProductsViewModel>()

    private val products: MutableList<Product> = mutableListOf()
    private val productsAdapter: ProductsAdapter = ProductsAdapter(products)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productsViewModel.getProducts()
        initObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        rvProducts.adapter = productsAdapter
        rvProducts.layoutManager = LinearLayoutManager(context)
    }

    private fun initObservers() {
        with(productsViewModel.getProductsViewState) {
            observe(loadingProducts) { handleLoadingProducts(it) }
            observe(products) { handleProducts(it) }
            observe(getProductsError) { handleGetProductsError() }
        }
    }

    private fun handleLoadingProducts(isLoading: Boolean) {
        if(isLoading) {
            clLoadingProducts.visible()
        } else {
            clLoadingProducts.gone()
        }
    }

    private fun handleProducts(list: List<Product>) {
        products.clear()
        products.addAll(list)
        productsAdapter.notifyDataSetChanged()
    }

    private fun handleGetProductsError() {
        showMessage(
            requireContext(),
            "Error",
            "Não foi possível carregar a lista de produtos",
            "Cancelar"
        ) {
            productsViewModel.getProducts()
        }
    }
}
