package com.simoneventrici.feedly.presentation.authentication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun ConfirmDialog(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack.copy(.5f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(LighterGray)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.confirm_check),
                    contentDescription = "Confirm",
                    modifier = Modifier.width(128.dp).height(120.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = text,
                    color = WhiteColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}