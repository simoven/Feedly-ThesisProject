package com.simoneventrici.feedly.presentation.stocks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.Stock
import com.simoneventrici.feedly.model.StockData
import com.simoneventrici.feedly.repository.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val stocksRepository: StocksRepository
): ViewModel() {

    val allStocks = mutableStateOf<List<Stock>>(emptyList())
    val favouriteStocks = mutableStateOf<DataState<List<StockData>>>(DataState.None())
    val isRefreshing = mutableStateOf(false)

    init {
        getAllStocks()
        getFavouriteStocks(Constants.TEST_TOKEN)
    }

    private fun getAllStocks() {
        viewModelScope.launch {
            allStocks.value = stocksRepository.getAllStocks()
        }
    }

    fun getFavouriteStocks(authToken: String) {
        stocksRepository.getUserFavouritesStocks(authToken).onEach {
            favouriteStocks.value = it
            println(it)
        }.launchIn(viewModelScope)
    }

    fun addStocksToFavourite(stocks: List<String>) {
        viewModelScope.launch {
            stocks.forEach { stock ->
                stocksRepository.addStockToFavourite(Constants.TEST_TOKEN, stock)
            }
            getFavouriteStocks(Constants.TEST_TOKEN)
        }
    }

    fun removeFavouriteStock(stock: String) {
        viewModelScope.launch {
            stocksRepository.removeStockFromFavourite(Constants.TEST_TOKEN, stock)
        }
        val oldList = favouriteStocks.value.data?.toMutableList() ?: mutableListOf()
        oldList.removeIf { it.ticker == stock }
        favouriteStocks.value = DataState.Success(data = oldList.toList())

    }
}