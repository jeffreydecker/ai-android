package com.itsdecker.androidai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itsdecker.androidai.database.typeconverter.SupportedModelTypeConverter

@Database(
    entities = [ChatModelEntity::class, ConversationEntity::class, MessageEntity::class],
    version = 1,
)
@TypeConverters(SupportedModelTypeConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatModelDao(): ChatModelDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao

    companion object {
        const val CHAT_DATABASE_NAME = "chat_database"
    }
}