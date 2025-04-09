package com.tbacademy.nextstep.domain.usecase.userSession

import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
import javax.inject.Inject

interface ClearValueFromLocalStorageUseCase {
    suspend operator fun invoke()
}

class ClearValueFromLocalStorageUseCaseImpl @Inject constructor(
    private val userSessionManagerRepository: UserSessionManagerRepository
) : ClearValueFromLocalStorageUseCase {
    override suspend fun invoke() {
        return userSessionManagerRepository.clear()

    }

}