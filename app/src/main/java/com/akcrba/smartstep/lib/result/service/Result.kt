package com.buchwald.coffeedate.lib.result.service

// import com.buchwald.coffeedate.lib.api.model.ApiBody
// import com.buchwald.coffeedate.lib.api.model.ApiErrorBody

sealed class Result<out T> {
    /*abstract fun <R> map(mapper: (T) -> R): Result<R>

    data class Success<out T>(val data: T, val meta: ApiBody.Meta? = null) : Result<T>() {
        override fun <R> map(mapper: (T) -> R): Success<R> = Success(mapper(data))
    }

     sealed class Error : Result<Nothing>() {
        override fun <R> map(mapper: (Nothing) -> R): Error = this

         sealed class Api : Error() {
             abstract val errors: List<ApiErrorBody.ApiError>
             abstract val httpStatusCode: Int

             data class Generic(
                override val errors: List<ApiErrorBody.ApiError>,
                override val httpStatusCode: Int,
            ) : Api()
        }

         data class Connectivity(val error: Throwable) : Error()

         data class Generic(val error: Throwable) : Error()
    }*/
}
