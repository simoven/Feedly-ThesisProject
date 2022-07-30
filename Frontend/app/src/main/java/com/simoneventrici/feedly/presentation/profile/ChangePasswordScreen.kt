package com.simoneventrici.feedly.presentation.profile

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.authentication.AuthViewModel
import com.simoneventrici.feedly.presentation.authentication.ErrorLabel
import com.simoneventrici.feedly.presentation.authentication.components.AuthButton
import com.simoneventrici.feedly.presentation.authentication.components.AuthTextField
import com.simoneventrici.feedly.presentation.authentication.components.ConfirmDialog
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val oldPasswordText = remember { mutableStateOf("") }
    val newPasswordText = remember { mutableStateOf("") }
    val confirmPasswordText = remember { mutableStateOf("") }

    val labelTextStyle = TextStyle(
        color = WhiteColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    BackHandler {
        navController.popBackStack()
        authViewModel.clearErrorMessages()
    }

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
        // la riga con il bottone indietro e il testo signup
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
                text = LocalContext.current.getString(R.string.change_password),
                color = WhiteColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))


        Text(
            text = LocalContext.current.getString(R.string.old_password),
            style = labelTextStyle,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(textState = oldPasswordText, onValueChange = { value -> oldPasswordText.value = value}, isPasswordField = true )


        Spacer(Modifier.height(20.dp))


        Text(
            text = LocalContext.current.getString(R.string.new_password),
            style = labelTextStyle,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(textState = newPasswordText, onValueChange = { value -> newPasswordText.value = value}, isPasswordField = true )


        Spacer(Modifier.height(20.dp))

        Text(
            text = LocalContext.current.getString(R.string.confirm_password),
            style = labelTextStyle,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(textState = confirmPasswordText, onValueChange = { value -> confirmPasswordText.value = value}, isPasswordField = true )


        Spacer(modifier = Modifier.weight(0.4f))

        AuthButton(
            text = LocalContext.current.getString(R.string.change_password),
            backgroundColor = MainGreen,
            onClick = {
                authViewModel.changePassword(oldPasswordText.value, newPasswordText.value, confirmPasswordText.value)
            }
        )

        if(authViewModel.genericErrorCode.value != -1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ErrorLabel(authViewModel.genericErrorCode.value)
            }
        }

        Spacer(modifier = Modifier.weight(1.2f))
    }


    if(authViewModel.showConfirmDialog.value) {
        ConfirmDialog(text = LocalContext.current.getString(R.string.password_change_successful))
    }
}