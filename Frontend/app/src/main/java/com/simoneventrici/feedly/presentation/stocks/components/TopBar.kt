package com.simoneventrici.feedly.presentation.stocks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun TopBar(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LighterBlack).padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow_icon),
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { navController.popBackStack() },
            contentDescription = "Back button",
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = LocalContext.current.getString(R.string.stocks),
            color = WhiteColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}