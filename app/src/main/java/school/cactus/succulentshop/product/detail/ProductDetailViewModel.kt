package school.cactus.succulentshop.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.Result.*
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.list.ProductListFragmentDirections

class ProductDetailViewModel(
    private val productId: Int,
    private val productRepository: ProductDetailRepository,
) : BaseViewModel() {

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product

    private val _relatedProducts = MutableLiveData<List<ProductItem>>()
    val relatedProducts: LiveData<List<ProductItem>> = _relatedProducts

    private val _isScreenVisible = MutableLiveData<Boolean>()
    val isScreenVisible: LiveData<Boolean> = _isScreenVisible

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible

    private val _isTitleVisible = MutableLiveData<Boolean>()
    val isTitleVisible: LiveData<Boolean> = _isTitleVisible

    val itemClickListener: (ProductItem) -> Unit = {
        val action = ProductDetailFragmentDirections.productDetailSelf(it.id)
        navigation.navigate(action)
    }

    init {
        setDefaultState()
        fetchData()
    }

    private fun fetchData() {
        fetchProduct()
        fetchRelatedProducts()
    }

    private fun fetchProduct() = viewModelScope.launch {
        productRepository.fetchProductDetail(productId).collect {
            when (it) {
                is Success -> {
                    onSuccess(it.value)
                    setState()
                }
                Loading -> onLoading()
                TokenExpired -> onTokenExpired()
                UnexpectedError -> onUnexpectedError()
                Failure -> onFailure()
            }
        }
    }

    private fun fetchRelatedProducts() = viewModelScope.launch {
        productRepository.fetchRelatedProducts(productId).collect {
            when (it) {
                is Success -> {
                    onRelatedProductsSuccess(it.value)
                    setState()
                }
                TokenExpired -> onTokenExpired()
                UnexpectedError -> onUnexpectedError()
                Failure -> onFailure()
            }
        }
    }

    private fun onSuccess(product: ProductItem) {
        _product.value = product
    }

    private fun onRelatedProductsSuccess(products: List<ProductItem>) {
        _relatedProducts.value = products
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
                    fetchData()
                }
            )
        )
    }

    private fun onLoading() {
        _isProgressBarVisible.value = true
    }

    private fun setDefaultState() {
        _isScreenVisible.value = false
        _isTitleVisible.value = false
    }

    private fun setState() {
        if (_product.value != null) {
            _isScreenVisible.value = true
            _isProgressBarVisible.value = false

            if (_relatedProducts.value != null && _relatedProducts.value!!.isNotEmpty()) {
                _isTitleVisible.value = true
            }
        }
    }

    private fun navigateToLogin() {
        val directions = ProductListFragmentDirections.tokenExpired()
        navigation.navigate(directions)
    }

    fun navigateToImagePage() {
        val action =
            ProductDetailFragmentDirections.detailtoProductImage(product.value!!.highResImageUrl)
        navigation.navigate(action)
    }
}