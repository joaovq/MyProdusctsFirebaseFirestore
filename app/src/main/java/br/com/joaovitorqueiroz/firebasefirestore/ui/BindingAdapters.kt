package br.com.joaovitorqueiroz.firebasefirestore.ui

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovitorqueiroz.firebasefirestore.R
import br.com.joaovitorqueiroz.firebasefirestore.model.Product
import br.com.joaovitorqueiroz.firebasefirestore.ui.products.ProductViewModel
import br.com.joaovitorqueiroz.firebasefirestore.ui.productslist.ProductsListAdapter
import br.com.joaovitorqueiroz.firebasefirestore.ui.productslist.ProductsListViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

@BindingAdapter("list")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Product>?) {
    val adapter = recyclerView.adapter as ProductsListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imgUrl")
fun loadImagesIntoRecyclerView(imageView : ImageView, imgUrl : String?) {
   imgUrl?.let {
       Glide.with(imageView.context)
           .load(imgUrl)
           .centerCrop()
           .placeholder(R.drawable.loading_animation)
           .error(R.drawable.ic_error)
           .into(imageView)
   }
}

@BindingAdapter("productsStatus")
fun bindStatusList(progressBar : ProgressBar, state : ProductsListViewModel.ProductsListState){

    val recyclerView = progressBar.rootView.findViewById<RecyclerView>(R.id.rvProducts)

    when (state) {
        ProductsListViewModel.ProductsListState.Loading -> {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
        ProductsListViewModel.ProductsListState.Success -> {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        is ProductsListViewModel.ProductsListState.Error -> {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            showSnackbar(progressBar.rootView.findViewById(R.id.coordinatorLayout), state.message)
        }
        is ProductsListViewModel.ProductsListState.Delete -> {
            showSnackbar(progressBar.rootView.findViewById(R.id.coordinatorLayout), state.message)
        }
    }
}

private fun showSnackbar(
    view : View,
    message: Int
) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

@BindingAdapter(value = ["status"], requireAll = false)
// The state not get value in construction. In the case, he need be null
fun bindStatusProduct(button: Button, state: ProductViewModel.ProductsState?){
    if (state != null){
        when(state){
            is ProductViewModel.ProductsState.Inserted -> {
                showSnackbar(button.rootView, state.message)
            }
            is ProductViewModel.ProductsState.Update -> {
                showSnackbar(button.rootView, state.message)
            }
        }
    }
}

