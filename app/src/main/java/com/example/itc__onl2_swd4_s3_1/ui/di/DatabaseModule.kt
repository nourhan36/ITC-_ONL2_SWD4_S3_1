package com.example.itc__onl2_swd4_s3_1.ui.di


import android.content.Context
import androidx.room.Room
import com.example.itc__onl2_swd4_s3_1.ui.data.room.RamadanTrackerDatabase
import com.example.itc__onl2_swd4_s3_1.ui.data.dao.DhikrDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.HabitDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.ProgressDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.SalahDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RamadanTrackerDatabase {
        return Room.databaseBuilder(
            context,
            RamadanTrackerDatabase::class.java,
            "RamadanTrackerDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDhikrDao(
        ramadanTrackerDatabase: RamadanTrackerDatabase
    ): DhikrDao {
        return ramadanTrackerDatabase.getDhikrDao()
    }

    @Provides
    @Singleton
    fun provideSalahDao(
        ramadanTrackerDatabase: RamadanTrackerDatabase
    ): SalahDao {
        return ramadanTrackerDatabase.getSalahDao()
    }

    @Provides
    @Singleton
    fun provideProgressDao(
        ramadanTrackerDatabase: RamadanTrackerDatabase
    ): ProgressDao {
        return ramadanTrackerDatabase.getProgressDao()
    }

    @Provides
    @Singleton
    fun provideHabitDao(
        ramadanTrackerDatabase: RamadanTrackerDatabase
    ): HabitDao {
        return ramadanTrackerDatabase.getHabitDao()
    }


}

