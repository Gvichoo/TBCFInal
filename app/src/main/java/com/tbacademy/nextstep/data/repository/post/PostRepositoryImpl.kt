package com.tbacademy.nextstep.data.repository.post

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : PostRepository {
    override suspend fun getPosts(): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading(true))
        try {

            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                emit(Resource.Error(ApiError.Unauthorized))
                return@flow
            }

            val currentUserId = currentUser.uid

            val postsSnapshot = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val posts: List<PostDto> = postsSnapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(PostDto::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    emit(Resource.Error(e.toApiError()))
                    return@flow
                }
            }

            // 2. Fetch user reactions
            val reactionsSnapshot = firestore.collection("reactions")
                .whereEqualTo("authorId", currentUserId)
                .get()
                .await()

            val reactionMap = reactionsSnapshot.documents.associateBy(
                { it.getString("postId") ?: "" },
                { it.getString("type") ?: "NONE" }
            )

            // 3. Merge: Map userReaction to each post
            val postsWithReactions = posts.map { dto ->
                val userReaction = reactionMap[dto.id] ?: "NONE"
                dto.toDomain().copy(userReaction = ReactionType.valueOf(userReaction))
            }

            emit(Resource.Success(postsWithReactions))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
        const val SORT_CREATED_AT = "createdAt"
    }
}