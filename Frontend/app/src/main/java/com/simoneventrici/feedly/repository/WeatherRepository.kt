package com.simoneventrici.feedly.repository

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.WeatherOverview
import com.simoneventrici.feedly.remote.api.WeatherAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WeatherRepository(
    private val weatherApi: WeatherAPI,
    private val context: Context,
    private val constants: Constants
) {

    fun getWeatherInfoByCoords(latitude: Double, longitude: Double): Flow<DataState<WeatherOverview>> = flow {
        try {
            emit(DataState.Loading<WeatherOverview>())
            val result = weatherApi.getWeatherInfoByCoords(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                authToken = constants.weatherApiKey as String,
                language = Locale.current.language
            ).toWeatherOverview()
            emit(DataState.Success(data = result))
        } catch(e: HttpException) {
            emit(DataState.Error<WeatherOverview>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<WeatherOverview>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }
}