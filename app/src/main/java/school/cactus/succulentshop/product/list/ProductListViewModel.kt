package school.cactus.succulentshop.product.list

import androidx.annotation.MenuRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.Result.*
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.product.ProductItem

class ProductListViewModel(
    private val store: JwtStore,
    private val repository: ProductListRepository
) : BaseViewModel() {

    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>> = _products

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    val itemClickListener: (ProductItem) -> Unit = {
        val action = ProductListFragmentDirections.openProductDetail(it.id)
        navigation.navigate(action)
    }

    val onRefreshListener: () -> Unit = {
        fetchProducts()
        _isRefreshing.value = false
    }

    init {
        fetchProducts()
    }

    fun fetchProducts() = viewModelScope.launch {
        _snackbarState.value = null

        repository.fetchProducts().collect {
            when (it) {
                is Success -> onSuccess(it.value)
                TokenExpired -> onTokenExpired()
                UnexpectedError -> onUnexpectedError()
                Failure -> onFailure()
                Loading -> onLoading()
            }
        }
    }

    private fun onSuccess(products: List<ProductItem>) {
        _products.postValue(products)
        _isProgressBarVisible.value = false
    }

    private fun onTokenExpired() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.your_session_is_expired,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.log_in,
                action = {
                    navigateToLogin()
                }
            )
        )
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = Snackbar.LENGTH_LONG,
        )
    }

    private fun onFailure() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    fetchProducts()
                }
            )
        )
    }

    private fun onLoading() {
        _isProgressBarVisible.value = true
    }

    fun menuItemClicked(@MenuRes id: Int) {
        if (id == R.id.logout) {
            store.deleteJwt()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val directions = ProductListFragmentDirections.tokenExpired()
        navigation.navigate(directions)
    }
}