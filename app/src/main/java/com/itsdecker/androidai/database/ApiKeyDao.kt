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
    @Query("SELECT * FROM ApiKey ORDER BY createdAt DESC LIMIT 1")
    fun getLatestApiKey(): ApiKeyEntity?

    @Query("SELECT * FROM ApiKey ORDER BY createdAt DESC")
    fun getAllApiKeys(): Flow<List<ApiKeyEntity>>

    @Query("SELECT * FROM ApiKey WHERE id = :apiKeyId")
    fun getChatModel(apiKeyId: String): ApiKeyEntity

    @Transaction
    @Query("SELECT * from ApiKey ORDER BY createdAt DESC")
    fun getAllConversations(): Flow<List<ApiKeyWithConversations>>

    @Transaction
    @Query("SELECT * FROM ApiKey WHERE id = :apiKeyId")
    fun getAllChatModelConversations(apiKeyId: String): Flow<List<ApiKeyWithConversations>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(apiKeyEntity: ApiKeyEntity)

    @Delete
    fun deleteChatModel(apiKeyEntity: ApiKeyEntity)

    @Query("DELETE FROM ApiKey")
    fun deleteChatModels()
}