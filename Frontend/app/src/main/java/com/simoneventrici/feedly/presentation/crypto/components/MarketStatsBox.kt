package com.simoneventrici.feedly.presentation.crypto.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.crypto.CryptoViewModel
import com.simoneventrici.feedly.ui.theme.WhiteColor
import com.simoneventrici.feedly.ui.theme.WhiteDark1

@Composable
fun StatsText(modifier: Modifier, title: Int, content: String) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = LocalContext.current.getString(title),
            color = WhiteColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            color = WhiteDark1,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        )

    }
}

// Inizialmente salvo l'altezza del box, se è stato rilevato lo scrollUp, setto la height del box a zero in modo da far espandere la card sotto
@Composable
fun MarketStatsBox(
    cryptoViewModel: CryptoViewModel,
) {
    val scrollUpState = cryptoViewModel.scrollUp.observeAsState()
    val heightInDp = with(LocalDensity.current) {
        cryptoViewModel.statsBoxHeight.value.toDp()
    }
    val height by animateDpAsState(targetValue = if(scrollUpState.value == true) 0.dp else heightInDp)

    val modifier = if(cryptoViewModel.statsBoxHeight.value == 0) {
        Modifier.onGloballyPositioned { cryptoViewModel.statsBoxHeight.value = it.size.height }
    } else
        Modifier.height(height)

    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = LocalContext.current.getString(R.string.global_stats),
            color = WhiteColor,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)) {
            StatsText(Modifier.weight(1f), title = R.string.n_coins, content = "1298")
            StatsText(Modifier.weight(1f),title = R.string.total_volume_24h, content = "\$ 12,789,210,230 ")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)) {
            StatsText(Modifier.weight(1f), title = R.string.n_coins, content = "1298")
            StatsText(Modifier.weight(1f), title = R.string.total_volume_24h, content = "\$ 12,789,210,230 ")
        }
        Spacer(Modifier.height(32.dp))
    }
}