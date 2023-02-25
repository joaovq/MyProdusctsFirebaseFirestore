package br.com.joaovitorqueiroz.firebasefirestore.ui.productslist

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.joaovitorqueiroz.firebasefirestore.R
import br.com.joaovitorqueiroz.firebasefirestore.data.database.DataSource
import br.com.joaovitorqueiroz.firebasefirestore.databinding.FragmentProductsListBinding
import br.com.joaovitorqueiroz.firebasefirestore.extensions.navigateWithAnim
import br.com.joaovitorqueiroz.firebasefirestore.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductsListFragment : Fragment() {

    private val viewModel: ProductsListViewModel by viewModels{
        object :ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = FirebaseFirestore.getInstance()

                val dataSource = DataSource(db)

                return ProductsListViewModel(dataSource) as T

            }
        }
    }

    private lateinit var _binding : FragmentProductsListBinding
    val binding : FragmentProductsListBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater)

//        DataBinding: Setting the fragment as the LifecycleOwner might cause
//        memory leaks because views lives shorter than the Fragment.
//        Consider using Fragment's view lifecycle
        _binding.lifecycleOwner = viewLifecycleOwner

        _binding.viewModel = viewModel

        _binding.rvProducts.setHasFixedSize(true)
        _binding.rvProducts.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        _binding.rvProducts.adapter = ProductsListAdapter(
            onClick = { product -> navigateUpdateProduct(product) },
            openLink = { product -> openLinkAtRecyclerView(product) }
        )

        return _binding.root
    }

    private fun openLinkAtRecyclerView(product: Product) {
        val intent = Intent(Intent.ACTION_VIEW, product.image.toUri())
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        swipeToDelete()
    }


    private fun navigateUpdateProduct(product: Product){
        val action = ProductsListFragmentDirections
            .actionProductsListFragmentToProductFragment(product)

        findNavController().navigateWithAnim(action)
    }

    private fun swipeToDelete(){
        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val swipeFlags = ItemTouchHelper.END
                val dragFlags = 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = _binding.rvProducts.adapter as ProductsListAdapter

                adapter.currentList.apply {
                    val product = get(viewHolder.adapterPosition)
                    product.id?.let { id ->
                        viewModel.deleteDocument(id)
                    }
                }
                viewModel.getAll()
            }

        }
        ItemTouchHelper(callback).attachToRecyclerView(_binding.rvProducts)
    }

    private fun setListeners() {
        _binding.floatingActionButtonAdd.setOnClickListener {
            val navController = findNavController()
            val action =
                ProductsListFragmentDirections.actionProductsListFragmentToProductFragment()
            navController.navigateWithAnim(action)
        }

        _binding.srlMain.apply{
            setOnRefreshListener {
                viewModel.getAll()
                isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }
}