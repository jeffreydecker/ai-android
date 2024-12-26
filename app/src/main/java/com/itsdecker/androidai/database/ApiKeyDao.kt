package com.itsdecker.androidai.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiKeyDao {
    @Query("SELECT * FROM ApiKey ORDER BY createdAt DESC")
    fun getAllChatModels(): Flow<List<ApiKeyEntity>>

    @Query("SELECT * FROM ApiKey WHERE id = :chatModelId")
    fun getChatModel(chatModelId: String): ApiKeyEntity

    @Transaction
    @Query("SELECT * FROM ApiKey WHERE id = :chatModelId")
    fun getAllChatModelConversations(chatModelId: String): Flow<ApiKeyWithConversation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(apiKeyEntity: ApiKeyEntity)

    @Delete
    fun deleteChatModel(apiKeyEntity: ApiKeyEntity)

    @Query("DELETE FROM ApiKey")
    fun deleteChatModels()
}