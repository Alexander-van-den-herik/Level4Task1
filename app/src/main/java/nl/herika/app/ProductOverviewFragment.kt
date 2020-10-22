package nl.herika.app

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.herika.app.databinding.FragmentProductOverviewBinding
import nl.herika.app.model.Product
import nl.herika.app.repository.ProductRepository

class ProductOverviewFragment : Fragment() {

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val products = arrayListOf<Product>()
    private lateinit var binding: FragmentProductOverviewBinding
    private val productAdapter =
        ProductAdapter(products)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productRepository = ProductRepository(requireContext())
        getShoppingListFromDatabase()

        binding = FragmentProductOverviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val products = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@ProductOverviewFragment.products.clear()
            this@ProductOverviewFragment.products.addAll(products)
            this@ProductOverviewFragment.productAdapter.notifyDataSetChanged()
        }
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
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title))
        val dialogLayout = layoutInflater.inflate(R.layout.fragment_add_product, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.et_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.et_amount)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.btn_add) { _: DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun addProduct(name: EditText, amount: EditText) {
        if (validateFields(name, amount)) {
            mainScope.launch {
                val product = Product(
                    name = name.text.toString(),
                    quantity = amount.text.toString().toLong()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getShoppingListFromDatabase()
            }
        }
    }

    private fun validateFields(txtProductName: EditText, txtAmount: EditText): Boolean {
        return if (txtProductName.text.toString().isNotBlank()
            && txtAmount.text.toString().isNotBlank()
        ) {
            true
        } else {
            Toast.makeText(activity, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun removeAllProducts() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
        }
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
            layoutManager = LinearLayoutManager(activity)
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

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(products[position])
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

}