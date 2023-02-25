package br.com.joaovitorqueiroz.firebasefirestore.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovitorqueiroz.firebasefirestore.R
import br.com.joaovitorqueiroz.firebasefirestore.data.repository.Repository
import br.com.joaovitorqueiroz.firebasefirestore.model.Product
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: Repository<String, List<Product>>) : ViewModel() {
    private val _stateProduct : MutableLiveData<ProductsState> = MutableLiveData()
    val stateProduct : LiveData<ProductsState> = _stateProduct

    fun addOrUpdate(product : Product,id : String? = null){
        if (id == null){
            addProduct(product)
        } else {
            updateProduct(product, id)
        }
    }

    private fun updateProduct(product : Product,id : String) {
        viewModelScope.launch {
            repository.update(product = product, id = id)
            _stateProduct.value = ProductsState.Update()
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.add(product)
            _stateProduct.value = ProductsState.Inserted()
        }
    }

    sealed class ProductsState {
        data class Inserted(val message : Int = R.string.message_inserted_firebase)
            : ProductsState()
        data class Update(val message : Int = R.string.message_updated_product_firebase)
            : ProductsState()
    }
}