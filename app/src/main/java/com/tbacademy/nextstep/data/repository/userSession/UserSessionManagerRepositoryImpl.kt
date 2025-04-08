package com.tbacademy.nextstep.data.repository.userSession

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSessionManagerRepositoryImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
): UserSessionManagerRepository{
    override suspend fun <T> saveValue(key: Preferences.Key<T>, value: T) {
        datastore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun <T> readValue(key: Preferences.Key<T>, value : T): Flow<T> {
        return datastore.data.map { preferences ->
            preferences[key] ?: value
        }
    }

    override suspend fun clear() {
        datastore.edit { it.clear() }
    }

}