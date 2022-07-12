package com.simoneventrici.feedly.presentation.stocks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun StockLoader(
    brush: Brush
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier.width(90.dp).height(20.dp).clip(RoundedCornerShape(8.dp)).background(brush)
        )
        Spacer(
            modifier = Modifier.width(110.dp).height(40.dp).clip(RoundedCornerShape(12.dp)).background(brush)
        )
        Spacer(
            modifier = Modifier.width(50.dp).height(30.dp).clip(RoundedCornerShape(8.dp)).background(brush)
        )
    }
}