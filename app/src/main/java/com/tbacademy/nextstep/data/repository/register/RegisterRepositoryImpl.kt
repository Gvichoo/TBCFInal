package com.tbacademy.nextstep.data.repository.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore : FirebaseFirestore
) : RegisterRepository{
    override fun register(nickname: String, email: String, password: String): Flow<Resource<Boolean>> = flow{
        try {
            emit(Resource.Loading(true))

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid
            emit(Resource.Success(true))
            if (userId != null) {

                val userMap = hashMapOf(
                    "nickname" to nickname,
                    "email" to email
                )
                fireStore.collection("users").document(userId).set(userMap).await()

                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("User ID is null"))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Loading(false))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Registration failed"))
            emit(Resource.Loading(false))
        }
    }
}