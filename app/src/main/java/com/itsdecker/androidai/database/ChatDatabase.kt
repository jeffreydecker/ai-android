package com.itsdecker.androidai.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itsdecker.androidai.database.typeconverter.SupportedProviderTypeConverter

@Database(
    entities = [
        ApiKeyEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
    ],
    version = 3,
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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add the new updatedAt colum to conversation
        db.execSQL("ALTER TABLE Conversation ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
        // Set the initial value of updatedAt to the value of createdAt
        db.execSQL("UPDATE Conversation SET updatedAt = createdAt")
    }
}