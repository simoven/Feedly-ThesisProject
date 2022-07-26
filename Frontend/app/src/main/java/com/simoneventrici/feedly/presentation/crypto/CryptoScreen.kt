package com.simoneventrici.feedly.presentation.crypto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.crypto.components.FavouriteCryptosBox
import com.simoneventrici.feedly.presentation.crypto.components.MarketStatsBox
import com.simoneventrici.feedly.presentation.crypto.components.TopBar
import com.simoneventrici.feedly.ui.theme.LighterBlack

@Composable
fun CryptoScreen(
    navController: NavController,
    cryptoViewModel: CryptoViewModel = hiltViewModel()
) {
    val isRefreshing by cryptoViewModel.isRefreshing
    val userToken = cryptoViewModel.userToken.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { cryptoViewModel.fetchFavouritesCrypto(userToken) }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                TopBar(navController = navController)

                Spacer(modifier = Modifier.height(20.dp))
                MarketStatsBox(cryptoViewModel)
            }
        }

        FavouriteCryptosBox(cryptoViewModel, navController)
    }
}