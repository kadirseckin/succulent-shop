package school.cactus.succulentshop.product.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import school.cactus.succulentshop.databinding.ItemRelatedBinding
import school.cactus.succulentshop.product.ProductItem

class RelatedProductsAdapter :
    ListAdapter<ProductItem, RelatedProductsAdapter.RelatedProductHolder>(DIFF_CALLBACK) {

    var itemClickListener: (ProductItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProductHolder {
        val binding = ItemRelatedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RelatedProductHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RelatedProductHolder, position: Int) =
        holder.bind(getItem(position))

    class RelatedProductHolder(
        private val binding: ItemRelatedBinding,
        private val itemClickListener: (ProductItem) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductItem) {
            binding.relatedProduct = product

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