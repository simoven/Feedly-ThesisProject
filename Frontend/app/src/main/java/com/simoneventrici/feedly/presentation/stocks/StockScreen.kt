package com.simoneventrici.feedly.presentation.stocks

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.components.ShimmerEffectLoader
import com.simoneventrici.feedly.presentation.crypto.components.CryptoInfoRow
import com.simoneventrici.feedly.presentation.crypto.components.FavouriteCryptosBox
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.presentation.stocks.components.StockComponent
import com.simoneventrici.feedly.presentation.stocks.components.StockLoader
import com.simoneventrici.feedly.presentation.stocks.components.TopBar
import com.simoneventrici.feedly.ui.theme.ChartRed
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun FavStocksTitle(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = LocalContext.current.getString(R.string.favourite_stocks),
            color = WhiteColor,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = LocalContext.current.getString(R.string.add_new_crypto),
            color = MainGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.clickable { navController.navigate(Screen.AddNewStockScreen.route) }
        )
    }
}

@Composable
fun StockScreen(
    navController: NavController,
    stocksViewModel: StocksViewModel = hiltViewModel()
) {
    val isRefreshing by stocksViewModel.isRefreshing
    val favStocks = stocksViewModel.favouriteStocks.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp),
        horizontalAlignment = Alignment.Start
    ) {
        TopBar(navController = navController)

        Spacer(modifier = Modifier.height(10.dp))
        FavStocksTitle(navController)
        Spacer(modifier = Modifier.height(15.dp))

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { stocksViewModel.getFavouriteStocks(Constants.TEST_TOKEN) }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if(favStocks is DataState.Success) {
                    favStocks.data?.let { stocksData ->
                        stocksData.forEach { stockData ->
                            val isRemoved = remember(stockData.ticker) { mutableStateOf(false) }

                            val rowOffset = animateFloatAsState(
                                targetValue = if(isRemoved.value) -2000f else 0f,
                                animationSpec = if(!isRemoved.value) tween(0) else tween(500)
                            ) {
                                if(it == -2000f) {
                                    stocksViewModel.removeFavouriteStock(stockData.ticker)
                                }
                            }

                            val removeFavourite = SwipeAction(
                                icon = painterResource(id = R.drawable.remove_icon),
                                onSwipe = { isRemoved.value = true },
                                background = ChartRed,
                            )
                            SwipeableActionsBox(
                                swipeThreshold = 60.dp,
                                endActions = listOf(removeFavourite),
                                backgroundUntilSwipeThreshold = Color.Transparent,
                            ) {
                                StockComponent(
                                    stockData = stockData,
                                    modifier = Modifier.graphicsLayer {
                                        translationX = rowOffset.value
                                    }
                                )
                            }
                        }
                    }
                }

                if(favStocks is DataState.Loading) {
                    repeat(8) {
                        ShimmerEffectLoader {
                            StockLoader(brush = it)
                        }
                    }
                }
            }
        }
    }
}