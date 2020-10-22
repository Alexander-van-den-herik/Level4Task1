package nl.herika.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.herika.app.databinding.FragmentProductOverviewBinding
import nl.herika.app.model.Product
import nl.herika.app.repository.ProductRepository

class ProductOverviewFragment : Fragment() {

    private val products = arrayListOf<Product>()
    private lateinit var binding: FragmentProductOverviewBinding
    private val productAdapter =
        ProductAdapter(products)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        binding.fabAddItem.setOnClickListener {
            showAddProductDialog();
        }


        binding.fabRemove.setOnClickListener {
            removeAllProducts()
        }


    }

    private fun showAddProductDialog() {
        TODO("Not yet implemented")
    }

    private fun removeAllProducts() {
        TODO("Not yet implemented")
    }


    private fun initData() {
        binding.rvProducts.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        createItemTouchHelper().attachToRecyclerView(binding.rvProducts)

        binding.rvProducts.apply {
            setHasFixedSize(true)
            layoutManager =  LinearLayoutManager(activity)
            adapter = productAdapter
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

            }
        }
        return ItemTouchHelper(callback)
    }

}