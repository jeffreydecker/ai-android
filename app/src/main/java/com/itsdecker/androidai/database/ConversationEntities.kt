package com.itsdecker.androidai.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(
    tableName = "Conversation",
    foreignKeys = [
        ForeignKey(
            entity = ApiKeyEntity::class,
            parentColumns = ["id"],
            childColumns = ["apiKeyId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
)
data class ConversationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val apiKeyId: String?,
    val title: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "Message",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("conversationId")]
)
data class MessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ConversationWithApiKey(
    @Relation(
        parentColumn = "apiKeyId",
        entityColumn = "id"
    )
    val apiKey: ApiKeyEntity,
    @Embedded
    val conversation: ConversationEntity,
)

data class ConversationWithMessages(
    @Embedded val conversation: ConversationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversationId"
    )
    val messages: List<MessageEntity>,
)