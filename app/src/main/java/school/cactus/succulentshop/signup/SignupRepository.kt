package school.cactus.succulentshop.signup

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Response
import school.cactus.succulentshop.Result
import school.cactus.succulentshop.Result.*
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.signup.SignupRequest
import school.cactus.succulentshop.api.signup.SignupResponse

class SignupRepository {
    suspend fun sendSignupRequest(
        email: String,
        password: String,
        username: String
    ): Result<String> {
        val request = SignupRequest(email, password, username)

        val response = try {
            api.signUp(request)
        } catch (ex: Exception) {
            null
        }

        return response.toResult()
    }

    private fun Response<SignupResponse>?.toResult() = when (this?.code()) {
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