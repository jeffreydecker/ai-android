package com.itsdecker.androidai.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.itsdecker.androidai.data.SupportedProvider

@Entity(tableName = "chat_models")
data class ChatModelEntity(
    @PrimaryKey val id: String,
    val createdAt: Long = System.currentTimeMillis(),
    val name: String,
    val description: String,
    val apiKey: String,
    val chatModel: SupportedProvider,
)

data class ChatModelWithConversations(
    @Embedded val model: ChatModelEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chatModelId"
    )
    val conversations: List<ConversationEntity>
)