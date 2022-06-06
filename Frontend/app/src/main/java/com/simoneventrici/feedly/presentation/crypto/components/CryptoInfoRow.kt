package com.simoneventrici.feedly.presentation.crypto.components

import android.widget.Space
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.commons.convertMarketCap
import com.simoneventrici.feedly.commons.convertNumberInCurrency
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.CryptoMarketData
import com.simoneventrici.feedly.presentation.components.LineChart
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun PercentageIndicator(label: String, percentage: Double) {
    Row {
        Text(
            text = label,
            fontSize = 14.sp,
            color = WhiteColor,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = String.format("%.2f", percentage) + " %",
            fontSize = 14.sp,
            color = if (percentage >= 0) ChartGreen else ChartRed,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CryptoInfoRow(
    crypto: Crypto,
    cryptoMarketData: CryptoMarketData
) {
    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                expanded.value = !expanded.value
            }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cryptoMarketData.imageUrl),
                contentDescription = "crypto logo",
                modifier = Modifier.size(38.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = crypto.name,
                    color = WhiteColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.Gray.copy(.5f))
                            .padding(horizontal = 3.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = cryptoMarketData.rank.toString(),
                            color = WhiteColor,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = crypto.ticker.uppercase(),
                        color = WhiteDark2,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            LineChart(
                values = cryptoMarketData.sparkline,
                modifier = Modifier
                    .width(120.dp)
                    .height(38.dp),
                strokeColor = if(cryptoMarketData.change7d < 0) ChartRed else ChartGreen,
                strokeDp = 2,
                showLabel = false,
                showGradient = false
            )
            Spacer(modifier = Modifier.weight(1.2f))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = convertNumberInCurrency(cryptoMarketData.currentPrice),
                    color = WhiteColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "M Cap ${convertMarketCap(cryptoMarketData.marketCap)}",
                    color = WhiteDark2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if(expanded.value) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PercentageIndicator(label = "1h", percentage = cryptoMarketData.change1h)
                PercentageIndicator(label = "24h", percentage = cryptoMarketData.change24h)
                PercentageIndicator(label = "7d", percentage = cryptoMarketData.change7d)
            }
        }
    }
}