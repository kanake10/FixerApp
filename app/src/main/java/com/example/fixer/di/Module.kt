package com.example.fixer.di

import android.content.Context
import androidx.room.Room
import com.example.core.dao.CurrencyDao
import com.example.core.db.AppDatabase
import com.example.currency.repo.CurrencyRepository
import com.example.currency.sources.LocalDataSource
import com.example.currency.sources.RemoteDataSource
import com.example.currencyimpl.repoimpl.CurrencyRepositoryImpl
import com.example.currencyimpl.sourceimpl.LocalDataSourceImpl
import com.example.currencyimpl.sourceimpl.RemoteDataSourceImpl
import com.example.network.FixerApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFixerApiService(retrofit: Retrofit): FixerApiService {
        return retrofit.create(FixerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currency_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCurrencyDao(database: AppDatabase): CurrencyDao {
        return database.currencyDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(currencyDao: CurrencyDao): LocalDataSource {
        return LocalDataSourceImpl(currencyDao)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: FixerApiService): RemoteDataSource {
        return RemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(remoteDataSource, localDataSource)
    }
}