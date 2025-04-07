package com.tbacademy.nextstep.data.repository.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : RegisterRepository {

    override fun register(nickname: String, email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))

        try {
            val usernameQuery = fireStore.collection("users")
                .whereEqualTo("username", nickname)
                .get()
                .await()

            if (!usernameQuery.isEmpty) {
                emit(Resource.Error("Username already taken"))
                return@flow
            }

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid

            if (uid != null) {
                val user = User(uid = uid, email = email, username = nickname)

                fireStore.collection("users").document(uid).set(user)

                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("User creation failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Sign-up failed: ${e.message}"))
        }
    }
}



