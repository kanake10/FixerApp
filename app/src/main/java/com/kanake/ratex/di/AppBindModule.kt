package com.kanake.ratex.di

import com.kanake.currency.repo.CurrencyRepository
import com.kanake.currency.sources.LocalDataSource
import com.kanake.currency.sources.RemoteDataSource
import com.kanake.currencyimpl.repoimpl.CurrencyRepositoryImpl
import com.kanake.currencyimpl.sourceimpl.LocalDataSourceImpl
import com.kanake.currencyimpl.sourceimpl.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindModule {

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        impl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(
        impl: CurrencyRepositoryImpl
    ): CurrencyRepository
}
