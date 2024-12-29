package com.itsdecker.androidai.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Transaction
    @Query("SELECT * FROM Conversation ORDER BY createdAt DESC")
    fun getAllConversationsWithApiKey(): Flow<List<ConversationWithApiKey>>

    @Transaction
    @Query("SELECT * FROM Conversation WHERE apiKeyId = :apiKeyId ORDER BY createdAt DESC")
    fun getAllConversationsWithApiKey(apiKeyId: String): Flow<List<ConversationWithApiKey>>

    @Transaction
    @Query("SELECT * FROM Conversation ORDER BY createdAt DESC")
    fun getAllConversationsWithMessages(): Flow<List<ConversationWithMessages>>

    @Transaction
    @Query("SELECT * FROM Conversation WHERE id = :conversationId")
    fun getConversationWithMessages(conversationId: String?): ConversationWithMessages?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Delete
    suspend fun deleteConversation(conversation: ConversationEntity)
}

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    // I think with the DB structure this ends up unnecessary
    // unless I want to clear a conversation but still keep it
    @Query("DELETE FROM Message WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversationId(conversationId: String)
}