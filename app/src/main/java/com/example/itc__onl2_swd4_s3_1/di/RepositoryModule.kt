package com.example.itc__onl2_swd4_s3_1.di

import com.example.itc__onl2_swd4_s3_1.data.repository.HabitRepositoryImpl
import com.example.itc__onl2_swd4_s3_1.data.repository.SalahRepositoryImpl
import com.example.itc__onl2_swd4_s3_1.domain.repository.HabitRepository
import com.example.itc__onl2_swd4_s3_1.domain.repository.SalahRepository
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
    abstract fun bindHabitRepository(
        impl: HabitRepositoryImpl
    ): HabitRepository

    @Binds
    @Singleton
    abstract fun bindSalahRepository(
        impl: SalahRepositoryImpl
    ): SalahRepository
}
