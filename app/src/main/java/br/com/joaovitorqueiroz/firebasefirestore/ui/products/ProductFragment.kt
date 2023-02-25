package br.com.joaovitorqueiroz.firebasefirestore.ui.products

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovitorqueiroz.firebasefirestore.R
import br.com.joaovitorqueiroz.firebasefirestore.data.database.DataSource
import br.com.joaovitorqueiroz.firebasefirestore.databinding.FragmentProductBinding
import br.com.joaovitorqueiroz.firebasefirestore.extensions.closeKeyBoard
import br.com.joaovitorqueiroz.firebasefirestore.model.Product
import br.com.joaovitorqueiroz.firebasefirestore.ui.productslist.ProductsListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class ProductFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels{
        object :ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = FirebaseFirestore.getInstance()

                val dataSource = DataSource(db)

                return ProductViewModel(dataSource) as T
            }
        }
    }

    private val args : ProductFragmentArgs by navArgs()

    private lateinit var _binding : FragmentProductBinding
    val binding : FragmentProductBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductBinding.inflate(inflater, container, false)

        //        DataBinding: Setting the fragment as the LifecycleOwner might cause
//        memory leaks because views lives shorter than the Fragment.
//        Consider using Fragment's view lifecycle
        _binding.lifecycleOwner = viewLifecycleOwner

        _binding.viewModel = viewModel

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.executePendingBindings()

        args.product?.let {
                product ->
            _binding.etProductNome.setText(product.nome)
            _binding.etProductValor.setText(product.valor.toString())
            _binding.etProductImage.setText(product.image)
            _binding.btnAdd.text = getString(R.string.text_btn_update_product_fragment)
        }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        _binding.btnAdd.setOnClickListener {
            if (isValidForm()) {
                val product = getProductIntance()
                viewModel.addOrUpdate(product, product.id)
                clearFields()
                findNavController().popBackStack()
            }
            hideKeyboard()
        }
    }

    private fun isValidForm() = when{
        _binding.etProductImage.text.toString().isBlank() -> {
            showErrorInView("This argument Image can't null")
            false
        }
        _binding.etProductNome.text.toString().isBlank() -> {
            showErrorInView("This argument Nome can't null")
            false
        }
        _binding.etProductValor.text.toString().isBlank() -> {
            showErrorInView("This argument Valor can't null")
            false
        }
        else -> true
    }

    private fun showErrorInView(message : String) {
        makeSnackbarWithMessage(message)
    }

    private fun makeSnackbarWithMessage(message: String) {
        Snackbar.make(_binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun clearFields() {
        _binding.etProductNome.text?.clear()
        _binding.etProductValor.text?.clear()
    }

    private fun getProductIntance() = Product(
        id = args.product?.id,
       nome =  _binding.etProductNome.text.toString(),
        valor = _binding.etProductValor.text.toString().toLongOrNull() ?: 0,
        image = _binding.etProductImage.text.toString()
    )

    private fun hideKeyboard(){
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity){
            parentActivity.closeKeyBoard()
        }
    }

}