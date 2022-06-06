package com.simoneventrici.feedly.presentation.crypto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.crypto.CryptoViewModel
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun FavouriteCryptosBox(
    cryptoViewModel: CryptoViewModel,
    navController: NavController
) {
    val cryptoListState = rememberLazyListState()

    val cryptoState = cryptoViewModel.favouritesCrypto
    val cryptoMarketData = cryptoViewModel.cryptosMarketData
    var dividerYOffset by remember { mutableStateOf(0f) }

    // questo modifier, se scrollato sopra o sotto, permette di espandere/comprimere la card di tutte le cripto
    val scrollableModifier = Modifier.pointerInput(Unit) {
        detectDragGestures { _, dragAmount ->
            dividerYOffset += dragAmount.y

            if(dividerYOffset >= 20f) {
                cryptoViewModel.dividerScrolled(false)
                dividerYOffset = 0f
            }
            if(dividerYOffset <= -20f) {
                cryptoViewModel.dividerScrolled(true)
                dividerYOffset = 0f
            }
        }
    }

    // quando cambia il primo elemento visibile, aggiorno il viewmodel per capire se devo espandere la card o no
    if(cryptoMarketData.value.keys.size > 12) {
        LaunchedEffect(key1 = cryptoListState.firstVisibleItemIndex) {
            cryptoViewModel.updateScrollPosition(cryptoListState.firstVisibleItemIndex)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            scrollableModifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LocalContext.current.getString(R.string.your_favourite_cryptos),
                color = WhiteColor,
                fontSize = 24.sp,
                fontWeight = Bold,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = LocalContext.current.getString(R.string.add_new_crypto),
                color = MainGreen,
                fontSize = 18.sp,
                fontWeight = W500,
                modifier = Modifier.clickable { navController.navigate(Screen.AddNewCryptoScreen.route) }
            )
        }
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
                    Spacer(scrollableModifier.height(10.dp))
                    Divider(
                        scrollableModifier
                            .height(4.dp)
                            .width(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(WhiteColor)
                    )
                    Spacer(scrollableModifier.height(8.dp))
                }
                items(items = cryptoState.value.data?.sortedBy { it.name } ?: emptyList()) { crypto ->
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