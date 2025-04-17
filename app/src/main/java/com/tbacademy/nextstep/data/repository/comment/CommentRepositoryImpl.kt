package com.tbacademy.nextstep.data.repository.comment

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
import com.tbacademy.nextstep.data.remote.dto.CommentDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: FirebaseHelper
) : CommentRepository {
    override suspend fun createComment(postId: String, text: String): Flow<Resource<Boolean>> {
        return firebaseHelper.withUserSnapshotFlow { userId, userSnapshot ->
            val commentRef = firestore.collection(COMMENT_COLLECTION_KEY).document()
            val username =
                userSnapshot.getString(USERNAME_FIELD_KEY) ?: throw FirebaseNoSignedInUserException(
                    "Username can't be found"
                )

            val commentDto = CommentDto(
                id = commentRef.id,
                postId = postId,
                authorId = userId,
                authorUsername = username,
                text = text
            )

            commentRef.set(commentDto).await()
            true
        }
    }

    companion object {
        const val COMMENT_COLLECTION_KEY = "comments"
        const val USERNAME_FIELD_KEY = "username"
    }
}