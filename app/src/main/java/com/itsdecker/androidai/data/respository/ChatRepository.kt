package com.itsdecker.androidai.data.respository

import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ChatDatabase
import com.itsdecker.androidai.database.ApiKeyEntity
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

    fun getApiKey(apiKeyId: String?) =
        apiKeyDao.getChatModel(apiKeyId = apiKeyId)

    fun getAllApiKeys() = apiKeyDao.getAllApiKeys()

    fun getLatestApiKey() = apiKeyDao.getLatestApiKey()

    fun getAllConversations(apiKeyId: String?) =
        when (apiKeyId) {
            null -> conversationDao.getAllConversationsWithApiKey()
            else -> conversationDao.getAllConversationsWithApiKey(apiKeyId)
        }

    fun getConversation(conversationId: String?) = conversationDao.getConversationWithMessages(conversationId)

    suspend fun createApiKey(
        name: String,
        description: String,
        apiKey: String,
        supportedProvider: SupportedProvider,
    ) = withContext(Dispatchers.IO) {
        val chatModel = ApiKeyEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            apiKey = apiKey,
            chatModel = supportedProvider,
        )
        apiKeyDao.insertModel(chatModel)
    }

    suspend fun deleteChatModels() = withContext(Dispatchers.IO) {
        apiKeyDao.deleteChatModels()
    }

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

    suspend fun addMessage(message: MessageEntity) = withContext(Dispatchers.IO) {
        messageDao.insertMessage(message)
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
}