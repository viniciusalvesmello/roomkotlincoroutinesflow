package io.github.viniciusalvesmello.roomkotlincoroutinesflow.view

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.R
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.model.Product
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.Constantes.PRODUCT_ID_LENGTH
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.Constantes.PRODUCT_ID_PAD_CHAR
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.inflate
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.toCurrencyFormatCurrency
import kotlinx.android.synthetic.main.row_product.view.*

class ProductsAdapter(
    private val products: List<Product>
) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(parent.inflate(viewType))

    override fun getItemViewType(position: Int): Int = R.layout.row_product

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(products[position])

    class ProductsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {
            view.tvId.text = product.id.toString().padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PAD_CHAR)
            view.tvDescription.text = product.description
            view.tvValue.text = product.value.toCurrencyFormatCurrency()

            view.setOnClickListener {
                it.findNavController().navigate(
                    ProductsFragmentDirections.actionProductsFragmentToProductFragment(product.id)
                )
            }
        }
    }
}