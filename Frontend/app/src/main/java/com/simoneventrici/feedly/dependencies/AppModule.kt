package com.simoneventrici.feedly.dependencies

import android.content.Context
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.remote.api.*
import com.simoneventrici.feedly.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient): NewsAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/news/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsRepository(newsAPI: NewsAPI, @ApplicationContext context: Context): NewsRepository {
        return NewsRepository(newsAPI, context)
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
    fun provideAuthRepository(authAPI: AuthAPI): AuthRepository {
        return AuthRepository(authAPI)
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
    fun providesActivityRepository(activityAPI: ActivityAPI, @ApplicationContext context: Context): ActivityRepository {
        return ActivityRepository(activityAPI, context)
    }

    @Provides
    @Singleton
    fun providesCryptoRepository(
        cryptoAPI: CryptoAPI,
        @ApplicationContext context: Context,
        coingeckoAPI: CoingeckoAPI,
        coinrankingAPI: CoinrankingAPI,
        constants: Constants
    ): CryptoRepository {
        return CryptoRepository(cryptoAPI, context, coingeckoAPI, coinrankingAPI, constants)
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

    @Provides
    @Singleton
    fun provideAppConstants(@ApplicationContext context: Context): Constants {
        return Constants(context)
    }

    @Provides
    @Singleton
    fun providesCoinrankingApi(): CoinrankingAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.COINRANKING_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinrankingAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePositionStackApi(): PositionStackAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.POSITIONSTACK_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PositionStackAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGeoLocalizationRepository(
        @ApplicationContext context: Context,
        positionStackAPI: PositionStackAPI,
        constants: Constants
    ): GeoLocalizationRepository {
        return GeoLocalizationRepository(positionStackAPI, context, constants)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        @ApplicationContext context: Context,
        weatherAPI: WeatherAPI,
        constants: Constants,
    ): WeatherRepository {
        return WeatherRepository(weatherAPI, context, constants)
    }

    @Provides
    @Singleton
    fun provideSoccerApi(): SoccerAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/soccer/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SoccerAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideSoccerRepository(
        @ApplicationContext context: Context,
        soccerAPI: SoccerAPI
    ): SoccerRepository {
        return SoccerRepository(soccerAPI, context)
    }

    @Provides
    @Singleton
    fun provideFinanceApi(): FinanceAPI {
        return Retrofit.Builder()
            .baseUrl("${Constants.FEEDLY_BACKEND_URL}/stocks/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideStocksRepository(
        @ApplicationContext context: Context,
        financeAPI: FinanceAPI
    ): StocksRepository {
        return StocksRepository(financeAPI, context)
    }
}