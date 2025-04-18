package com.tbacademy.nextstep.data.httpHelper

import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseHelper @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun <T> withUserFlow(
        action: suspend (userId: String) -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading(true))
        val user = firebaseAuth.currentUser
        if (user == null) {
            emit(Resource.Error(ApiError.Unauthorized))
        } else {
            try {
                val result = action(user.uid)
                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(e.toApiError()))
            }
        }
        emit(Resource.Loading(false))
    }
}