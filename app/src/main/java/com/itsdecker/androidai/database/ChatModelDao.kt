package com.itsdecker.androidai.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatModelDao {
    @Query("SELECT * FROM chat_models ORDER BY createdAt DESC")
    fun getAllChatModels(): Flow<List<ChatModelEntity>>

    @Transaction
    @Query("SELECT * FROM chat_models WHERE id = :chatModelId")
    fun getAllChatModelConversations(chatModelId: String): Flow<ChatModelWithConversations>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(chatModelEntity: ChatModelEntity)

    @Delete
    fun deleteChatModel(chatModelEntity: ChatModelEntity)
}