package com.example.itc__onl2_swd4_s3_1.ui.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRamadanTrackerRepository(
        ramadanTrackerRepositoryImpl: RamadanTrackerRepositoryImpl
    ): RamadanTrackerRepository

}