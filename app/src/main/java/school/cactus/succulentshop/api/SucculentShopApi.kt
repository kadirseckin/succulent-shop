package school.cactus.succulentshop.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.api.product.RelatedProduct
import school.cactus.succulentshop.api.signup.SignupRequest
import school.cactus.succulentshop.api.signup.SignupResponse

interface SucculentShopApi {
    @POST("/auth/local")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/local/register")
    suspend fun signUp(@Body signupRequest: SignupRequest): Response<SignupResponse>

    @GET("/products")
    suspend fun listAllProducts(): Response<List<Product>>

    @GET("/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @GET("/related-products/{id}")
    suspend fun getRelatedProductsById(@Path("id") id: Int): Response<RelatedProduct>
}