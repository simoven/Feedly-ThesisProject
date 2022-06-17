package com.simoneventrici.feedly.presentation.soccer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun TopBar(
    navController: NavController
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow_icon),
            contentDescription = "back button",
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = LocalContext.current.getString(R.string.soccer),
            color = WhiteColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}