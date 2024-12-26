package com.itsdecker.androidai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itsdecker.androidai.database.typeconverter.SupportedProviderTypeConverter

@Database(
    entities = [
        ApiKeyEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
    ],
    version = 2,
)
@TypeConverters(SupportedProviderTypeConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun apiKeyDao(): ApiKeyDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao

    companion object {
        const val CHAT_DATABASE_NAME = "chat_database"
    }
}