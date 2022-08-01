package com.simoneventrici.feedly.presentation.authentication

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.authentication.components.AuthButton
import com.simoneventrici.feedly.presentation.authentication.components.AuthTextField
import com.simoneventrici.feedly.presentation.authentication.components.ConfirmDialog
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun PasswordResetScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val emailState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(
                top = getSystemStatusbarHeightInDp(LocalContext.current).dp + 20.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 30.dp
            ),
        horizontalAlignment = Alignment.Start
    ) {
        // la riga con il bottone indietro e il testo login
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow_rounded),
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.popBackStack(); authViewModel.clearErrorMessages() },
                contentDescription = "back button"
            )
            Spacer(Modifier.width(30.dp))
            Text(
                text = LocalContext.current.getString(R.string.reset_password),
                color = WhiteColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "E-mail",
            color = WhiteColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        AuthTextField(
            textState = emailState,
            onValueChange = { value -> emailState.value = value }
        )


        if(authViewModel.emailErrorCode.value != -1) {
            ErrorLabel(authViewModel.emailErrorCode.value)
        }

        Spacer(modifier = Modifier.height(20.dp))

        AuthButton(
            text = LocalContext.current.getString(R.string.confirm),
            backgroundColor = MainGreen,
            onClick = {
                authViewModel.handlePasswordReset(emailState.value)
            }
        )
    }


    if(authViewModel.showConfirmDialog.value) {
        ConfirmDialog(text = LocalContext.current.getString(R.string.email_sent))
    }
}