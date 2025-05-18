package com.example.itc__onl2_swd4_s3_1.di

import com.example.itc__onl2_swd4_s3_1.data.repository.PrayerTimesRepositoryImpl
import com.example.itc__onl2_swd4_s3_1.domain.repository.PrayerTimesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PrayerModule {
    @Binds
    @Singleton
    abstract fun bindPrayerTimesRepository(
        impl: PrayerTimesRepositoryImpl
    ): PrayerTimesRepository
}
