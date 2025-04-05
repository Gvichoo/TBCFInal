package com.tbacademy.nextstep.data.repository.register

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RegisterRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : RegisterRepository {

    override fun register(nickname: String, email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))

        try {
            val result = suspendCancellableCoroutine<Task<AuthResult>> { continuation ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception("Registration failed"))
                        }
                    }
            }

            val uid = result.result?.user?.uid
            uid?.let {
                val userMap = mapOf(
                    "username" to nickname,
                    "email" to email
                )

                suspendCancellableCoroutine { continuation ->
                    fireStore.collection("Users").document(it).set(userMap)
                        .addOnSuccessListener {
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }

                emit(Resource.Success(true))
            } ?: emit(Resource.Error("User ID not found"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Registration failed"))
        }

        emit(Resource.Loading(false))
    }
}


