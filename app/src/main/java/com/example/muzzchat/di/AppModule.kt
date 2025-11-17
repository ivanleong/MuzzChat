package com.example.muzzchat.di

import android.app.Application
import androidx.room.Room
import com.example.muzzchat.data.local.ChatMessageDao
import com.example.muzzchat.data.local.ChatMessagesDatabase
import com.example.muzzchat.data.repository.ChatRepositoryImpl
import com.example.muzzchat.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ChatMessagesDatabase =
        Room.databaseBuilder(app, ChatMessagesDatabase::class.java, "chat_message_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideChatDao(db: ChatMessagesDatabase): ChatMessageDao = db.chatMessageDao()

    @Provides
    fun provideChatRepository(dao: ChatMessageDao): ChatRepository =
        ChatRepositoryImpl(dao)

}