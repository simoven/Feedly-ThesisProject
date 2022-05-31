package com.simoneventrici.feedly.presentation.explore.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun ScrollableTopBar(
    scrollUpState: State<Boolean?>
) {
    val position by animateFloatAsState(if (scrollUpState.value == true) -150f else 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
            .graphicsLayer { translationY = position },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if(position > -140f) {
            Text(
                text = LocalContext.current.getString(R.string.explore_btn_content),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = WhiteColor
            )
            Image(
                contentDescription = "Search icon",
                painter = painterResource(id = R.drawable.search_unchecked),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}