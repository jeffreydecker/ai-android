package com.itsdecker.androidai.di

import android.content.Context
import androidx.room.Room
import com.itsdecker.androidai.database.ChatDatabase
import com.itsdecker.androidai.database.ChatDatabase.Companion.CHAT_DATABASE_NAME
import com.itsdecker.androidai.database.MIGRATION_2_3
import com.itsdecker.androidai.network.anthropic.AnthropicApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase =
        Room.databaseBuilder(
            context = context,
            klass = ChatDatabase::class.java,
            name = CHAT_DATABASE_NAME,
        )
            .addMigrations(MIGRATION_2_3)
            .build()
}