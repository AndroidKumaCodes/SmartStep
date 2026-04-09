package com.akcrba.smartstep.lib.core.di

import android.content.Context
import androidx.room3.Room
import com.akcrba.smartstep.feature.myprofile.data.local.SmartStepDatabase
import com.akcrba.smartstep.feature.myprofile.data.local.dao.UserDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
internal class DatabaseModule {

    @Singleton
    fun provideRoomDatabase(context: Context): SmartStepDatabase {
        return Room.databaseBuilder(
            context,
            SmartStepDatabase::class.java,
            "smartstep_database",
        )
            .fallbackToDestructiveMigration() // If you don't care about losing user data when the scheme changes
            .build()
    }

    @Singleton
    fun provideUserDao(database: SmartStepDatabase): UserDao {
        return database.userDao
    }
}
