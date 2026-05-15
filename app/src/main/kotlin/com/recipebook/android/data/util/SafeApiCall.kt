package com.recipebook.android.data.util

import com.recipebook.android.domain.util.Resource

suspend fun <T> safeApiCall(call: suspend () -> T): Resource<T> =
    try {
        Resource.Success(call())
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Неизвестная ошибка")
    }
