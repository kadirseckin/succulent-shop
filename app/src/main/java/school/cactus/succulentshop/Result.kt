package school.cactus.succulentshop

sealed class Result<out T : Any> {
    class Success<out T : Any>(val value: T) : Result<T>()
    class ClientError<out T : Any>(val value: T) : Result<T>()
    object TokenExpired : Result<Nothing>()
    object UnexpectedError : Result<Nothing>()
    object Failure : Result<Nothing>()
    object Loading : Result<Nothing>()
}