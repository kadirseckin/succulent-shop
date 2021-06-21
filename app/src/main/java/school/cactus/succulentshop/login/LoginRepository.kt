package school.cactus.succulentshop.login

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Response
import school.cactus.succulentshop.Result
import school.cactus.succulentshop.Result.*
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse

class LoginRepository {
    suspend fun sendLoginRequest(
        identifier: String,
        password: String
    ): Result<String> {
        val request = LoginRequest(identifier, password)

        val response = try {
            api.login(request)
        } catch (ex: Exception) {
            null
        }

        return response.toResult()
    }

    private fun Response<LoginResponse>?.toResult() = when (this?.code()) {
        null -> Failure
        200 -> Success(this.body()!!.jwt)
        in 400..499 -> ClientError(this.errorBody()!!.errorMessage())
        else -> UnexpectedError
    }

    private fun ResponseBody.errorMessage(): String {
        val errorBody = string()
        val gson: Gson = GsonBuilder().create()
        val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return loginErrorResponse.message[0].messages[0].message
    }
}