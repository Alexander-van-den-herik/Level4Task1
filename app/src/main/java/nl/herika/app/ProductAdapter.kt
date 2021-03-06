package nl.herika.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.herika.app.databinding.ItemProductBinding
import nl.herika.app.model.Product

class ProductAdapter (private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemProductBinding.bind(itemView)

        fun dataBind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvQuantity.text = product.quantity.toString() + " X"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
