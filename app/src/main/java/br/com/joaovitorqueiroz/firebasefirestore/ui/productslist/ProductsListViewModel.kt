package br.com.joaovitorqueiroz.firebasefirestore.ui.productslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovitorqueiroz.firebasefirestore.R
import br.com.joaovitorqueiroz.firebasefirestore.data.repository.Repository
import br.com.joaovitorqueiroz.firebasefirestore.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ProductsListViewModel(
    private val repository: Repository<String, List<Product>>
) : ViewModel() {

    private val _allProducts : MutableLiveData<List<Product>> =
        MutableLiveData()
    val allProducts : LiveData<List<Product>> get() = _allProducts
    private val _state : MutableLiveData<ProductsListState> =
        MutableLiveData()
    val state : LiveData<ProductsListState> = _state


    init {
        getAll()
    }

    fun getAll() {
             _state.value = ProductsListState.Loading
             try {
//                 _allProducts.value = repository.getAll()

                 FirebaseFirestore.getInstance().collection("products")
                     .get()
                     .addOnCompleteListener { task ->
                         if (task.isSuccessful){
                             val mutableListOf = mutableListOf<Product>()
                             try {
                                 for (i in task.result){
                                     mutableListOf.add(Product(i.id,i.data["nome"].toString(),
                                         i.data["valor"] as Long,i.data["imagem"].toString())
                                     )
                                 }

                                 _state.value = ProductsListState.Success
                             } catch (e : ClassCastException) {
                                 e.printStackTrace()
                                 Log.e("ERROR IN CODE", "Cast Failed")

                                 _state.value =
                                     ProductsListState.Error(R.string.message_error_in_class_cast)
                             } catch (e : NullPointerException) {
                                 _state.value =
                                     ProductsListState.Error(
                                         R.
                                         string.
                                         message_partial_get_data_error_products
                                     )

                                 Log.e("ERROR", "There data null in db")
                             } catch (e : Exception) {
                                 _state.value =
                                     ProductsListState.Error(
                                         R.
                                         string.
                                         message_internet_get_data_error_products
                                     )

                                 Log.e("ERROR", "Internet is off")
                             }
                             _allProducts.value = mutableListOf
                         }
                     }
             } catch (e : Exception) {
                 e.printStackTrace()
                 _allProducts.value = listOf()
                 _state.value = ProductsListState.Error()
             }
         }

    fun deleteDocument(id : String){
        viewModelScope.launch {
            try {
                repository.delete(id)
                _state.value = ProductsListState.Delete()
            } catch (e : Exception) {
                e.printStackTrace()
                _state.value = ProductsListState.Error(R.string.message_delete_error_product)
            }
        }
    }

    sealed class ProductsListState{
        object Success : ProductsListState()
        object Loading : ProductsListState()
        data class Error(val message : Int = R.string.message_error_in_get_data) : ProductsListState()
        data class Delete (
            val message : Int = R.string.message_delete_complete_product
        ) : ProductsListState()
    }

}