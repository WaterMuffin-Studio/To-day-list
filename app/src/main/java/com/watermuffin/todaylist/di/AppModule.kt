package com.watermuffin.todaylist.di

import android.content.Context
import androidx.room.Room
import com.watermuffin.todaylist.data.database.TodayListDatabase
import com.watermuffin.todaylist.data.database.dao.UserDao
import com.watermuffin.todaylist.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodayListDatabase {
        return Room.databaseBuilder(
            context,
            TodayListDatabase::class.java,
            "todaylist-db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: TodayListDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
}