package com.simoneventrici.feedly.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.presentation.authentication.AuthViewModel
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel
) {
    Column() {
        Spacer(Modifier.height(30.dp))
        Text(
            text = "Logout",
            color = WhiteColor,
            fontSize = 24.sp,
            modifier = Modifier.clickable {
                authViewModel.doLogout()
            }
        )
    }
}