package com.tbacademy.nextstep.domain.core

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: ApiError) : Resource<Nothing>()
    data class Loading(val loading: Boolean) : Resource<Nothing>()
}