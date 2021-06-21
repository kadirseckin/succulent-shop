package school.cactus.succulentshop.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import school.cactus.succulentshop.databinding.ItemProductBinding
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.list.ProductAdapter.ProductHolder

class ProductAdapter : ListAdapter<ProductItem, ProductHolder>(DIFF_CALLBACK) {

    var itemClickListener: (ProductItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) =
        holder.bind(getItem(position))

    class ProductHolder(
        private val binding: ItemProductBinding,
        private val itemClickListener: (ProductItem) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductItem) {
            binding.product = product

            binding.root.setOnClickListener {
                itemClickListener(product)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem) =
                oldItem == newItem
        }
    }
}