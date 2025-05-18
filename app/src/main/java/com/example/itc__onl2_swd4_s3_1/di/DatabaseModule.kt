package com.example.itc__onl2_swd4_s3_1.di

import android.content.Context
import androidx.room.Room
import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.data.local.database.SalahDatabase
import com.example.itc__onl2_swd4_s3_1.data.local.dao.CompletedDayDao
import com.example.itc__onl2_swd4_s3_1.data.local.dao.HabitDao
import com.example.itc__onl2_swd4_s3_1.data.local.dao.UserSettingsDao
import com.example.itc__onl2_swd4_s3_1.data.local.dao.SalahDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(@ApplicationContext context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            "habit_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSalahDatabase(@ApplicationContext context: Context): SalahDatabase {
        return Room.databaseBuilder(
            context,
            SalahDatabase::class.java,
            "salah_db"
        ).build()
    }

    @Provides
    fun provideHabitDao(db: HabitDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideCompletedDayDao(db: HabitDatabase): CompletedDayDao = db.completedDayDao()

    @Provides
    fun provideUserSettingsDao(db: HabitDatabase): UserSettingsDao = db.userSettingsDao()

    @Provides
    fun provideSalahDao(db: SalahDatabase): SalahDao = db.salahDao()
}
