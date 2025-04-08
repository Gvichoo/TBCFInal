package com.tbacademy.nextstep.domain.repository.userSession

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface UserSessionManagerRepository {
    suspend fun <T> saveValue(key: Preferences.Key<T>, value: T)

    fun <T> readValue(key: Preferences.Key<T>, value : T): Flow<T>

    suspend fun clear()
}