package com.tbacademy.nextstep.domain.usecase.userSession

import androidx.datastore.preferences.core.Preferences
import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
import javax.inject.Inject

interface SaveValueToLocalStorageUseCase {
    suspend operator fun <T> invoke(key: Preferences.Key<T>, value : T)
}

class SaveValueToLocalStorageUseCaseImpl @Inject constructor(
    private val userSessionManagerRepository: UserSessionManagerRepository
) : SaveValueToLocalStorageUseCase {
    override suspend operator fun <T> invoke(key: Preferences.Key<T>, value : T) {
        return userSessionManagerRepository.saveValue(key,value)
    }
}