package com.itsdecker.androidai.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.itsdecker.androidai.data.SupportedProvider

@Entity(tableName = "ApiKey")
data class ApiKeyEntity(
    @PrimaryKey val id: String,
    val createdAt: Long = System.currentTimeMillis(),
    val name: String,
    val description: String,
    val apiKey: String,
    val chatModel: SupportedProvider,
)

data class ApiKeyWithConversation(
    @Embedded val model: ApiKeyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chatModelId"
    )
    val conversations: List<ConversationEntity>
)