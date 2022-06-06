package com.simoneventrici.feedly.presentation.crypto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navController = navController)

        Spacer(modifier = Modifier.height(20.dp))
        MarketStatsBox(cryptoViewModel)

        FavouriteCryptosBox(cryptoViewModel, navController)
    }
}