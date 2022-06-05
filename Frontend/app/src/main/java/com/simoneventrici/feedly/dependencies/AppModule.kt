package com.simoneventrici.feedly.dependencies

import android.content.Context
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.remote.api.*
import com.simoneventrici.feedly.repository.ActivityRepository
import com.simoneventrici.feedly.repository.CryptoRepository
import com.simoneventrici.feedly.repository.NewsRepository
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
class AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStorePreferences {
        return DataStorePreferences(context)
    }

    @Provides
    @Singleton
    fun provideNewsApi(): NewsAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/news/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthAPI(): AuthAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesActivityAPI(): ActivityAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/activity/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivityAPI::class.java)
    }
    @Provides
    @Singleton
    fun providesCryptoAPI(): CryptoAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/crypto/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsRepository(newsAPI: NewsAPI, @ApplicationContext context: Context): NewsRepository {
        return NewsRepository(newsAPI, context)
    }

    @Provides
    @Singleton
    fun providesActivityRepository(activityAPI: ActivityAPI, @ApplicationContext context: Context): ActivityRepository {
        return ActivityRepository(activityAPI, context)
    }

    @Provides
    @Singleton
    fun providesCryptoRepository(
        cryptoAPI: CryptoAPI,
        @ApplicationContext context: Context,
        coingeckoAPI: CoingeckoAPI
    ): CryptoRepository {
        return CryptoRepository(cryptoAPI, context, coingeckoAPI)
    }

    @Provides
    @Singleton
    fun provideCoingeckoAPI(): CoingeckoAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.COINGECKO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoingeckoAPI::class.java)
    }
}