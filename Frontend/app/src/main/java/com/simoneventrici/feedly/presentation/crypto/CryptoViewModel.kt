package com.simoneventrici.feedly.presentation.crypto

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.CryptoMarketData
import com.simoneventrici.feedly.model.CryptoMarketStats
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository,
    private val preferences: DataStorePreferences
): ViewModel() {
    
    val favouritesCrypto = mutableStateOf<DataState<List<Crypto>>>(DataState.None())
    val cryptosMarketData = mutableStateOf<Map<String, CryptoMarketData>>(mapOf())
    val cryptoGlobalMarketStats = mutableStateOf<DataState<CryptoMarketStats>>(DataState.None())
    val allCryptos = mutableStateOf<List<Crypto>>(emptyList())
    val isRefreshing = mutableStateOf(false)
    val userToken = mutableStateOf("")

    // è l'altezza in dp del marketStatsBox
    val statsBoxHeight = mutableStateOf(0)
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    init {
        observeTokenChanges()
        fetchAllCryptos()
        fetchMarketStats()
    }

    private fun observeTokenChanges() {
        preferences.tokensFlow.onEach { token ->
            userToken.value = token ?: ""
            if(token?.isNotBlank() == true)
                fetchFavouritesCrypto(userToken.value)
        }.launchIn(viewModelScope)
    }

    private fun fetchAllCryptos() {
        viewModelScope.launch {
            allCryptos.value = cryptoRepository.getAllCryptos()
        }
    }

    fun fetchFavouritesCrypto(authToken: String) {
        cryptoRepository.getUserFavouritesCrypto(authToken).onEach {
            favouritesCrypto.value = it
            if(it is DataState.Success)
                fetchCryptoMarketData()
        }.launchIn(viewModelScope)
    }

    private fun fetchMarketStats() {
        cryptoRepository.getGlobalMarketStats().onEach {
            cryptoGlobalMarketStats.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchCryptoMarketData() {
        favouritesCrypto.value.data?.run {
            cryptoRepository.getCryptoMarketData(this.map { it.statsId }).onEach { res ->
                if(res is DataState.Success) {
                    // per ogni cripto per cui ho richiesto i dati
                    this.forEach { crypto ->
                        // trovo i market data nella lista di risposta
                        res.data?.find { it.ticker == crypto.ticker }?.run {
                            // se li ho trovati, li aggiungo alla mappa
                            val oldMap = cryptosMarketData.value.toMutableMap()
                            oldMap[ticker] = this
                            cryptosMarketData.value = oldMap
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun addCryptosToFavourite(cryptos: List<Crypto>) {
        viewModelScope.launch {
            cryptos.forEach { crypto ->
                if(cryptoRepository.addCryptoToFavourite(userToken.value, crypto.ticker) && favouritesCrypto.value is DataState.Success) {
                    val oldList = favouritesCrypto.value.data?.toMutableList() ?: mutableListOf()
                    oldList.add(crypto)
                    val newState = DataState.Success(data = oldList.toList())
                    favouritesCrypto.value = newState
                }
            }
            fetchFavouritesCrypto(userToken.value)
        }
    }

    fun removeFavouriteCrypto(crypto: Crypto) {
        viewModelScope.launch {
            cryptoRepository.removeCryptoFromFavourite(userToken.value, crypto.ticker)
        }
        val oldList = favouritesCrypto.value.data?.toMutableList() ?: mutableListOf()
        oldList.removeIf { it.ticker == crypto.ticker }
        favouritesCrypto.value = DataState.Success(data = oldList.toList())

    }

    fun updateScrollPosition(position: Int) {
        if (position > 0 && _scrollUp.value == false)
            _scrollUp.value = true

        if(position == 0 && _scrollUp.value == true)
            _scrollUp.value = false
    }

    fun dividerScrolled(isUp: Boolean) {
        _scrollUp.value = isUp
    }
}