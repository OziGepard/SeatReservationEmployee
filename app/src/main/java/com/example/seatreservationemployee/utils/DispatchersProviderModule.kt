package com.example.seatreservationemployee.utils

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DispatchersProviderModule {
    @Binds
    @Singleton
    fun bindDispatchesProvider(impl: DispatchesProviderImpl): DispatchesProvider
}