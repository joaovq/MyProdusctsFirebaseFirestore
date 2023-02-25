package br.com.joaovitorqueiroz.firebasefirestore.ui.productslist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovitorqueiroz.firebasefirestore.databinding.ItemProductListBinding
import br.com.joaovitorqueiroz.firebasefirestore.model.Product

class ProductsListAdapter(
    private val onClick : (Product) -> Unit,
    private val openLink : (Product) -> Unit
)
    : ListAdapter<Product, ProductsListAdapter.ProductListViewHolder>(DiffCallback) {
    inner class ProductListViewHolder(private var binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product  = product
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            //binding.executePendingBindings()
            binding.executePendingBindings()

            setListenersAtAdapter(product)
        }

        private fun setListenersAtAdapter(product: Product) {
            binding.root.setOnClickListener {
                onClick(product)
            }
            binding.imageView.setOnClickListener {
                openLink(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(
            ItemProductListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.nome == newItem.nome && oldItem.valor == newItem.valor
        }
    }

}