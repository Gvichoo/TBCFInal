package com.tbacademy.nextstep.domain.usecase.login

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> {
        return loginRepository.login(email, password)
    }
}