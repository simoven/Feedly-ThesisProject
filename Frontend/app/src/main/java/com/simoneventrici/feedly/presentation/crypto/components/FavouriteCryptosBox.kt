package com.simoneventrici.feedly.presentation.crypto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.crypto.CryptoViewModel
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.LighterGray2
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun FavouriteCryptosBox(
    cryptoViewModel: CryptoViewModel,
) {
    val cryptoListState = rememberLazyListState()

    val cryptoState = cryptoViewModel.favouritesCrypto
    val cryptoMarketData = cryptoViewModel.cryptosMarketData

    // quando cambia il primo elemento visibile, aggiorno il viewmodel per capire se devo espandere la card o no
    LaunchedEffect(key1 = cryptoListState.firstVisibleItemIndex) {
        cryptoViewModel.updateScrollPosition(cryptoListState.firstVisibleItemIndex)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = LocalContext.current.getString(R.string.your_favourite_cryptos),
            color = WhiteColor,
            fontSize = 24.sp,
            fontWeight = Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(LighterGray.copy(0.6f))
        ) {
            LazyColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = cryptoListState
            ) {
                item {
                    Spacer(Modifier.height(10.dp))
                    Divider(
                        Modifier.height(4.dp).width(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(WhiteColor)
                    )
                    Spacer(Modifier.height(8.dp))
                }
                items(items = cryptoState.value.data ?: emptyList()) { crypto ->
                    cryptoMarketData.value[crypto.ticker]?.run {
                        CryptoInfoRow(crypto = crypto, cryptoMarketData = this)
                        Divider(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth(.95f), color = LighterBlack.copy(.3f))
                    }
                }
            }
        }
    }
}