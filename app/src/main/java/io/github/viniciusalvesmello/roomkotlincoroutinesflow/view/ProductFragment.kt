package io.github.viniciusalvesmello.roomkotlincoroutinesflow.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.concrete.canarinho.formatador.Formatador
import br.com.concrete.canarinho.watcher.ValorMonetarioWatcher
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.R
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.Constantes.PRODUCT_ID_LENGTH
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.Constantes.PRODUCT_ID_PAD_CHAR
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.gone
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.observe
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.visible
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.showMessage
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_product.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProductFragment : Fragment(R.layout.fragment_product) {

    private val productsViewModel by sharedViewModel<ProductsViewModel>()

    private val productId: Int by lazy {
        arguments?.getInt(ARGUMENT_PRODUCT_ID, 0) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObservers()
        initListeners()
    }

    private fun initView() {
        tvProductId.text = productId.toString().padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PAD_CHAR)
        productsViewModel.getProduct(productId)
    }

    private fun initObservers() {
        with(productsViewModel.getProductViewState) {
            observe(loadingProduct) { showLoading(it) }
            observe(product) { handleProduct(it) }
            observe(getProductError) { handleGetProductError() }
        }

        observe(productsViewModel.loadingSaveProduct) { showLoading(it) }
        observe(productsViewModel.saveProduct) { handleSaveProduct(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            clLoadingProduct.visible()
        } else {
            clLoadingProduct.gone()
        }
    }

    private fun handleProduct(product: Product) {
        tvProductId.text = product.id.toString().padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PAD_CHAR)
        tietProductDescription.setText(product.description)
        tietProductDescription.setSelection(product.description.length)
        tietProductValue.setText(Formatador.VALOR_COM_SIMBOLO.formata(product.value.toString()))
        tietProductValue.setSelection(tietProductValue.text.toString().length)
    }

    private fun handleGetProductError() {
        showMessage(
            requireContext(),
            "Error",
            "Não foi possível recuperar os dados do produto ${tvProductId.text}!"
        ) {
            findNavController().navigateUp()
        }
    }

    private fun handleSaveProduct(isSavedWithSuccess: Boolean) {
        if (isSavedWithSuccess) {
            findNavController().navigateUp()
        } else {
            showMessage(
                requireContext(),
                "Error",
                "Não foi possível salvar o produto!"
            )
        }
    }

    private fun initListeners() {
        tietProductValue.addTextChangedListener(
            ValorMonetarioWatcher.Builder()
                .comSimboloReal()
                .comMantemZerosAoLimpar()
                .build()
        )

        mbProductSave.setOnClickListener {
            if (validateForm()) {
                productsViewModel.saveProduct(
                    Product(
                        id = productId,
                        description = tietProductDescription.text.toString().trim(),
                        value = Formatador.VALOR_COM_SIMBOLO.desformata(
                            tietProductValue.text.toString().trim()
                        ).toDouble()
                    )
                )
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        tilProductDescription.error = null
        tilProductValue.error = null

        if (tietProductDescription.text.toString().trim().isEmpty()) {
            tilProductDescription.error = getString(R.string.error_description_required)
            valid = false
        }

        if (tietProductValue.text.toString().trim().isEmpty()) {
            tilProductValue.error = getString(R.string.error_value_required)
            valid = false
        }

        return valid
    }

    companion object {
        private const val ARGUMENT_PRODUCT_ID = "product_id"
    }
}
