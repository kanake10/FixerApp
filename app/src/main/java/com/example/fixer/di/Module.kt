/*
 * Copyright 2025 Ezra Kanake.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.fixer.di

import android.content.Context
import androidx.room.Room
import com.example.core.Constants.API_KEY
import com.example.core.Constants.BASE_URL
import com.example.core.Constants.DB_NAME
import com.example.core.dao.CurrencyDao
import com.example.core.db.CurrencyDatabase
import com.example.currency.repo.CurrencyRepository
import com.example.currency.sources.LocalDataSource
import com.example.currency.sources.RemoteDataSource
import com.example.currencyimpl.repoimpl.CurrencyRepositoryImpl
import com.example.currencyimpl.sourceimpl.LocalDataSourceImpl
import com.example.currencyimpl.sourceimpl.RemoteDataSourceImpl
import com.example.network.ApiInterceptor
import com.example.network.FixerApiService
import com.example.network.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ApiInterceptor(API_KEY))
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideFixerApiService(retrofit: Retrofit): FixerApiService {
        return retrofit.create(FixerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CurrencyDatabase {
        return Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCurrencyDao(database: CurrencyDatabase): CurrencyDao {
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
        networkChecker: NetworkChecker
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(remoteDataSource, localDataSource, networkChecker)
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }
}
