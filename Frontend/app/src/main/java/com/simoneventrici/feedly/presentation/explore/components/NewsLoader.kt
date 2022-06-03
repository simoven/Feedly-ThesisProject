package com.simoneventrici.feedly.presentation.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

// è il componente con effetto shimmer per le notizie
@Composable
fun NewsLoader(
    brush: Brush
) {
    Spacer(Modifier.height(20.dp))
    Column(
        modifier = Modifier.fillMaxWidth(.9f)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .height(20.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Divider()
    }
}