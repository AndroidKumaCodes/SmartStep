package com.buchwald.coffeedate.lib.result.domain

sealed interface Result<out T, out E> {
    data class Success<T>(val data: T) : Result<T, Nothing>
    data class Failure<E>(val error: E) : Result<Nothing, E>
}

fun <T> T.toSuccess(): Result.Success<T> = Result.Success(this)
