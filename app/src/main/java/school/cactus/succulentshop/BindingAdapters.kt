package school.cactus.succulentshop

import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.detail.RelatedProductsAdapter
import school.cactus.succulentshop.product.list.ProductAdapter
import school.cactus.succulentshop.product.list.ProductDecoration

@BindingAdapter("app:error")
fun TextInputLayout.error(@StringRes errorMessage: Int?) {
    error = errorMessage?.resolveAsString(context)
    isErrorEnabled = errorMessage != null
}

val productAdapter = ProductAdapter()
val relatedProductsAdapter = RelatedProductsAdapter()

@BindingAdapter("app:products", "app:itemClickListener")
fun RecyclerView.products(products: List<ProductItem>?, itemClickListener: (ProductItem) -> Unit) {
    if (adapter != productAdapter) {
        adapter = productAdapter
    }

    productAdapter.itemClickListener = itemClickListener

    if (itemDecorationCount == 0) {
        addItemDecoration(ProductDecoration())
    }

    productAdapter.submitList(products.orEmpty())
}

@BindingAdapter("app:relatedProducts", "app:itemClickListener")
fun RecyclerView.relatedProducts(
    relatedProducts: List<ProductItem>?,
    itemClickListener: (ProductItem) -> Unit
) {
    if (adapter != relatedProductsAdapter)
        adapter = relatedProductsAdapter

    relatedProductsAdapter.itemClickListener = itemClickListener

    relatedProductsAdapter.submitList(relatedProducts.orEmpty())
}

@BindingAdapter("app:isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("app:setRefreshListener", "app:isRefreshing")
fun SwipeRefreshLayout.setRefreshListener(swipeListener: () -> Unit, isRefreshing: Boolean) {
    this.setOnRefreshListener(swipeListener)
    this.isRefreshing = isRefreshing
}

@BindingAdapter("app:setHeight")
fun ImageView.setSize(width: Int, height: Int) {
    layoutParams.height = height
}