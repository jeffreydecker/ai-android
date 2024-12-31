package com.itsdecker.androidai.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.itsdecker.androidai.data.SupportedProvider
import java.util.UUID

@Entity(tableName = "ApiKey")
data class ApiKeyEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val createdAt: Long = System.currentTimeMillis(),
    val name: String,
    val description: String,
    val apiKey: String,
    val chatModel: SupportedProvider, // TODO - Rename me
) {
    companion object {
        fun empty() : ApiKeyEntity = ApiKeyEntity (
            name = "",
            description = "",
            apiKey = "",
            chatModel = SupportedProvider.UNINITIALIZED,
        )
    }
}

data class ApiKeyWithConversations(
    @Embedded val apiKey: ApiKeyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "apiKeyId"
    )
    val conversations: List<ConversationEntity>
)