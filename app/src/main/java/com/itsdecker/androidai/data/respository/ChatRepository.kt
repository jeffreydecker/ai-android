package com.itsdecker.androidai.data.respository

import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ChatDatabase
import com.itsdecker.androidai.database.ChatModelEntity
import com.itsdecker.androidai.database.ConversationEntity
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
    private val chatModelsDao = database.chatModelDao()
    private val conversationDao = database.conversationDao()
    private val messageDao = database.messageDao()

    fun getChatModel(chatModelId: String) =
        chatModelsDao.getChatModel(chatModelId = chatModelId)

    fun getAllChatModels() = chatModelsDao.getAllChatModels()

    fun getAllConversations(chatModelId: String) =
        chatModelsDao.getAllChatModelConversations(chatModelId)

    fun getConversation(id: String) = conversationDao.getConversationWithMessages(id)

    suspend fun createApiKey(
        name: String,
        description: String,
        apiKey: String,
        supportedProvider: SupportedProvider,
    ) = withContext(Dispatchers.IO) {
        val chatModel = ChatModelEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            apiKey = apiKey,
            chatModel = supportedProvider,
        )
        chatModelsDao.insertModel(chatModel)
    }

    suspend fun deleteChatModels() = withContext(Dispatchers.IO) {
        chatModelsDao.deleteChatModels()
    }

    suspend fun createConversation(chatModelId: String, title: String? = null): String  = withContext(Dispatchers.IO) {
        val conversation = ConversationEntity(
            id = UUID.randomUUID().toString(),
            chatModelId = chatModelId,
            title = title,
        )
        conversationDao.insertConversation(conversation)
        conversation.id
    }

    suspend fun addMessage(conversationId: String, role: String, content: String) {
        val message = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = role,
            content = content
        )
        messageDao.insertMessage(message)
    }

    suspend fun deleteConversation(conversationId: String, chatModelId: String) {
        conversationDao.deleteConversation(
            ConversationEntity(
                id = conversationId,
                chatModelId = chatModelId
            )
        )
    }
}