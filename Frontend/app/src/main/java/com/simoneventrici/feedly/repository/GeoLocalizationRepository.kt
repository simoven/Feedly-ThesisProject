package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.CryptoMarketStats
import com.simoneventrici.feedly.model.GeoLocalizationInfo
import com.simoneventrici.feedly.remote.api.PositionStackAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GeoLocalizationRepository(
    private val positionStackAPI: PositionStackAPI,
    private val context: Context,
    private val constants: Constants
) {

    fun getGeolocalizationDataFromAddress(address: String): Flow<DataState<List<GeoLocalizationInfo>>> = flow {
        try {
            emit(DataState.Loading<List<GeoLocalizationInfo>>())
            val res = positionStackAPI.getLocalizationInfoFromAddress(constants.positionStackApiKey as String, address).geoData.map { it.toGeolocalizationInfo() }
            emit(DataState.Success(res))
        } catch(e: HttpException) {
            emit(DataState.Error<List<GeoLocalizationInfo>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<GeoLocalizationInfo>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }
}