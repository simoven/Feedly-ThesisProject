package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.GeoLocalizationInfo
import com.simoneventrici.feedly.remote.api.PositionStackAPI
import retrofit2.HttpException
import java.io.IOException

class GeoLocalizationRepository(
    private val positionStackAPI: PositionStackAPI,
    private val context: Context,
    private val constants: Constants
) {

    suspend fun getGeolocalizationDataFromAddress(address: String): List<GeoLocalizationInfo> {
        return try {
            positionStackAPI.getLocalizationInfoFromAddress(constants.positionStackApiKey as String, address).geoData.map { it.toGeolocalizationInfo() }
        } catch(e: Exception) {
            emptyList()
        }
    }
}