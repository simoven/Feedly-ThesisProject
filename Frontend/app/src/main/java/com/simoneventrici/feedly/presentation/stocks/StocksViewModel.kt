package com.simoneventrici.feedly.presentation.stocks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.Stock
import com.simoneventrici.feedly.model.StockData
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val stocksRepository: StocksRepository,
    private val preferences: DataStorePreferences
): ViewModel() {

    val allStocks = mutableStateOf<List<Stock>>(emptyList())
    val favouriteStocks = mutableStateOf<DataState<List<StockData>>>(DataState.None())
    val isRefreshing = mutableStateOf(false)
    val userToken = mutableStateOf("")

    init {
        observeTokenChanges()
        getAllStocks()
    }

    private fun observeTokenChanges() {
        preferences.tokensFlow.onEach { token ->
            userToken.value = token ?: ""

            if(token?.isNotBlank() == true)
                getFavouriteStocks()
        }.launchIn(viewModelScope)
    }


    private fun getAllStocks() {
        viewModelScope.launch {
            allStocks.value = stocksRepository.getAllStocks()
        }
    }

    fun getFavouriteStocks() {
        stocksRepository.getUserFavouritesStocks(userToken.value).onEach {
            favouriteStocks.value = it
        }.launchIn(viewModelScope)
    }

    fun addStocksToFavourite(stocks: List<String>) {
        viewModelScope.launch {
            stocks.forEach { stock ->
                stocksRepository.addStockToFavourite(userToken.value, stock)
            }
            getFavouriteStocks()
        }
    }

    fun removeFavouriteStock(stock: String) {
        viewModelScope.launch {
            stocksRepository.removeStockFromFavourite(userToken.value, stock)
        }
        val oldList = favouriteStocks.value.data?.toMutableList() ?: mutableListOf()
        oldList.removeIf { it.ticker == stock }
        favouriteStocks.value = DataState.Success(data = oldList.toList())

    }
}