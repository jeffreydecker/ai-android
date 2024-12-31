package com.itsdecker.androidai.data.respository

import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ChatDatabase
import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    database: ChatDatabase,
) {
    private val apiKeyDao = database.apiKeyDao()
    private val conversationDao = database.conversationDao()
    private val messageDao = database.messageDao()

    // API Keys

    suspend fun createApiKey(apiKey: ApiKeyEntity) =
        withContext(Dispatchers.IO) { apiKeyDao.insertModel(apiKey) }

    fun getAllApiKeys() = apiKeyDao.getAllApiKeys()

    suspend fun getApiKey(apiKeyId: String?) = withContext(Dispatchers.IO) {
        apiKeyDao.getApiKey(apiKeyId = apiKeyId)
    }

    fun getLatestApiKey() = apiKeyDao.getLatestApiKey()

    suspend fun deleteApiKey(apiKey: ApiKeyEntity) = withContext(Dispatchers.IO) {
        apiKeyDao.deleteApiKey(apiKey)
    }

    suspend fun deleteApiKeys() = withContext(Dispatchers.IO) {
        apiKeyDao.deleteApiKeys()
    }

    // Conversations

    suspend fun createConversation(
        apiKeyId: String,
        title: String? = null,
    ): ConversationWithMessages = withContext(Dispatchers.IO) {
        val conversation = ConversationEntity(
            id = UUID.randomUUID().toString(),
            apiKeyId = apiKeyId,
            title = title,
        )
        conversationDao.insertConversation(conversation)
        ConversationWithMessages(
            conversation = conversation,
            messages = emptyList(),
        )
    }

    fun getAllConversations(apiKeyId: String?) =
        when (apiKeyId) {
            null -> conversationDao.getAllConversationsWithApiKey()
            else -> conversationDao.getAllConversationsWithApiKey(apiKeyId)
        }

    fun getConversation(conversationId: String?) =
        conversationDao.getConversationWithMessages(conversationId)

    suspend fun updateConversation(conversation: ConversationEntity) = withContext(Dispatchers.IO) {
        conversationDao.updateConversation(conversation)
    }

    suspend fun deleteConversation(
        conversationId: String,
        apiKeyId: String,
    ) = withContext(Dispatchers.IO) {
        conversationDao.deleteConversation(
            ConversationEntity(
                id = conversationId,
                apiKeyId = apiKeyId
            )
        )
    }

    // Messages

    suspend fun addMessage(message: MessageEntity) = withContext(Dispatchers.IO) {
        messageDao.insertMessage(message)
    }
}