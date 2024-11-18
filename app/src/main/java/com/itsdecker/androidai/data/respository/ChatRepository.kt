package com.itsdecker.androidai.data.respository

import com.itsdecker.androidai.database.ChatDatabase
import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.MessageEntity
import java.util.UUID

class ChatRepository(private val database: ChatDatabase) {
    private val conversationDao = database.conversationDao()
    private val messageDao = database.messageDao()

    // TODO - I think maybe these should be suspend functions
    fun getAllConversations() = conversationDao.getAllConversations()
    fun getConversation(id: String) = conversationDao.getConversationWithMessages(id)

    suspend fun createConversation(title: String? = null): String {
        val conversation = ConversationEntity(
            id = UUID.randomUUID().toString(),
            title = title
        )
        conversationDao.insertConversation(conversation)
        return conversation.id
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

    suspend fun deleteConversation(conversationId: String) {
        conversationDao.deleteConversation(ConversationEntity(id = conversationId))
    }
}