package com.tbacademy.nextstep.data.repository.post

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper.Companion.SORT_CREATED_AT
import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: FirebaseHelper
) : PostRepository {
    override suspend fun getPosts(): Flow<Resource<List<Post>>> {
        return firebaseHelper.withUserIdFlow { userId ->

            val postsSnapshot = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val postDtoList = postsSnapshot.documents.mapNotNull {
                it.toObject(PostDto::class.java)?.copy(id = it.id)
            }

            val postIds = postDtoList.map { it.id }

            val reactionsSnapshot = firestore.collection(REACTIONS_COLLECTION_PATH)
                .whereEqualTo(AUTHOR_ID_FIELD, userId)
                .whereIn(POST_ID_FIELD, postIds)
                .get()
                .await()

            val userReactionsMap = reactionsSnapshot.documents.mapNotNull { doc ->
                val postId = doc.getString(POST_ID_FIELD) ?: return@mapNotNull null
                val typeString = doc.getString(REACTION_TYPE_FIELD) ?: return@mapNotNull null

                val reactionType = runCatching { ReactionType.valueOf(typeString) }.getOrNull()
                    ?: return@mapNotNull null

                postId to reactionType
            }.toMap()

            val postsWithReactionType = postDtoList.map { postDto ->
                val userReaction = userReactionsMap[postDto.id]
                postDto.toDomain().copy(userReaction = userReaction)
            }

            postsWithReactionType
        }
    }


    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
        const val REACTIONS_COLLECTION_PATH = "reactions"
        const val AUTHOR_ID_FIELD = "authorId"
        const val POST_ID_FIELD = "postId"
        const val REACTION_TYPE_FIELD = "type"
    }
}