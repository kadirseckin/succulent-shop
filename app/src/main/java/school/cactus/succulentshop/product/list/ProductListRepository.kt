package school.cactus.succulentshop.product.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import school.cactus.succulentshop.Result
import school.cactus.succulentshop.Result.*
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.db.db
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.toProductEntityList
import school.cactus.succulentshop.product.toProductItemList

class ProductListRepository {

    suspend fun fetchProducts(): Flow<Result<List<ProductItem>>> = flow {

        emit(Loading)

        val cachedProducts = db.productDao().getAll()

        if (cachedProducts.isNotEmpty()) {
            emit(Success(cachedProducts.toProductItemList()))
        }

        val response = try {
            api.listAllProducts()
        } catch (ex: Exception) {
            null
        }

        when (response?.code()) {
            null -> emit(Failure)
            200 -> {
                val productItems = response.body()!!.toProductItemList()
                emit(Success(productItems))
                db.productDao().insertAll(productItems.toProductEntityList())
            }
            401 -> emit(TokenExpired)
            else -> emit(UnexpectedError)
        }
    }
}