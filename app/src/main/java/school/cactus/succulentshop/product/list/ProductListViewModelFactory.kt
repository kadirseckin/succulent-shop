package school.cactus.succulentshop.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore

@Suppress("UNCHECKED_CAST")
class ProductListViewModelFactory(
    private val store: JwtStore,
    private val repository: ProductListRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ProductListViewModel(store, repository) as T
}