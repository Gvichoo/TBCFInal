package com.tbacademy.nextstep.data.repository.login

import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : LoginRepository{
    override fun login(email: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading(true))
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            emit(Resource.Success(result.user != null))

            emit(Resource.Loading(false))

        }catch (e : Exception) {
            emit(Resource.Error("Login failed: ${e.message}"))

            emit(Resource.Loading(false))
        }
    }
}