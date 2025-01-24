package com.android.data.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T, val source: Source) : Result<T>()
    data class Failure(val exception: Exception, val source: Source) : Result<Nothing>()
    data class Loading(val source: Source) : Result<Nothing>()
}

enum class Source {
    LOCAL,
    REMOTE
}