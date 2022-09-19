package com.simoneventrici.feedly.presentation.stocks.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.commons.convertNumberInCurrency
import com.simoneventrici.feedly.model.StockData
import com.simoneventrici.feedly.presentation.components.LineChart
import com.simoneventrici.feedly.ui.theme.ChartGreen
import com.simoneventrici.feedly.ui.theme.ChartRed
import com.simoneventrici.feedly.ui.theme.WhiteColor
import com.simoneventrici.feedly.ui.theme.WhiteDark2

@Composable
fun StockComponent(
    stockData: StockData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.width(150.dp)) {
            Text(
                text = stockData.name,
                color = WhiteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(0.dp, 100.dp)
            )
            Spacer(Modifier.width(10.dp))

            Text(
                text = stockData.ticker.uppercase(),
                color = WhiteDark2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        LineChart(
            values = stockData.prices,
            modifier = Modifier
                .width(110.dp)
                .height(36.dp),
            strokeColor = if(stockData.prices[0] <= stockData.prices.last()) ChartGreen else ChartRed,
            strokeDp = 2,
            showGradient = false,
            showLabel = false
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = convertNumberInCurrency(if(stockData.prices.isNotEmpty()) stockData.prices.last() else 0.0),
                color = WhiteColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "24h",
                    color = WhiteDark2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = String.format("%.2f", stockData.change24h) + " %",
                    color = if(stockData.change24h >= 0) ChartGreen else ChartRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}