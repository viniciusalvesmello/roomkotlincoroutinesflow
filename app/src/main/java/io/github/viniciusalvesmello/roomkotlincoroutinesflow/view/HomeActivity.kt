package io.github.viniciusalvesmello.roomkotlincoroutinesflow.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.R
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension.hideKeyboard
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    //private val productsViewModel by viewModel<ProductsViewModel>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navController = findNavController(R.id.fHome)

        initListener()
    }

    private fun initListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productsFragment -> {
                    mtHome.navigationIcon = null
                    mtHome.title = getString(R.string.list_of_products)
                    fabHome.visibility = View.VISIBLE
                    hideKeyboard()
                }
                R.id.productFragment -> {
                    mtHome.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
                    mtHome.title = getString(R.string.product_detail)
                    fabHome.visibility = View.INVISIBLE
                }
            }
        }

        fabHome.setOnClickListener {
            navController.navigate(
                ProductsFragmentDirections.actionProductsFragmentToProductFragment()
            )
        }

        mtHome.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}
