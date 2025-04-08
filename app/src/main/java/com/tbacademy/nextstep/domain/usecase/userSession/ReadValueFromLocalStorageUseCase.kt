package com.tbacademy.nextstep.domain.usecase.userSession

import androidx.datastore.preferences.core.Preferences
import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ReadValueFromLocalStorageUseCase {
    operator fun <T> invoke(key: Preferences.Key<T>, value: T): Flow<T>
}


class ReadValueFromLocalStorageUseCaseImpl @Inject constructor(
    private val userSessionManagerRepository: UserSessionManagerRepository
) : ReadValueFromLocalStorageUseCase {
    override operator fun <T> invoke(key: Preferences.Key<T>, value: T): Flow<T> {
        return userSessionManagerRepository.readValue(key, value)
    }
}