package com.buchwald.coffeedate.lib.result.service

// import com.ioki.textref.TextRef

fun printText(): Unit = println("Hello World")

/*
fun <T : Any, R : Any> Result<T>.mapResult(
    onSuccess: (T) -> R,
    onApiError: (Result.Error.Api) -> R,
    onConnectivityError: (Result.Error.Connectivity) -> R,
    onGenericError: (Result.Error.Generic) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(this.data)
    is Result.Error.Api.Generic -> onApiError(this)
    is Result.Error.Connectivity -> onConnectivityError(this)
    is Result.Error.Generic -> onGenericError(this)
}

val Result.Error.Api.userErrorMessage: TextRef?
    get() = errors.firstOrNull()?.message?.let { TextRef.string(it) }*/
