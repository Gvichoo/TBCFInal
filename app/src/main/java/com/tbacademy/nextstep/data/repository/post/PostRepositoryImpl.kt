package com.tbacademy.nextstep.data.repository.post

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PostRepository {
    override suspend fun getPosts(): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading(true))
        try {
            val snapshot = firestore.collection("posts")
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { doc ->
                try {

                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            }

            Log.d("HOME_STATE_REPO", "$posts")

            emit(Resource.Success(posts))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}