package com.simoneventrici.feedly.presentation.authentication

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
import com.simoneventrici.feedly.presentation.authentication.components.AuthButton
import com.simoneventrici.feedly.presentation.authentication.components.AuthTextField
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun ErrorLabel(errorCode: Int) {
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = LocalContext.current.getString(errorCode),
        color = ChartRed,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val usernameText = remember { mutableStateOf("") }
    val passwordText = remember { mutableStateOf("") }

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
                text = LocalContext.current.getString(R.string.login),
                color = WhiteColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))



        // Username text field
        Text(
            text = "Username",
            style = labelTextStyle,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(textState = usernameText, onValueChange = { value -> usernameText.value = value} )

        // error riguardante username
        if(authViewModel.usernameErrorCode.value != -1) {
            ErrorLabel(authViewModel.usernameErrorCode.value)
        }

        Spacer(Modifier.height(20.dp))



        // Password text field
        Text(
            text = "Password",
            style = labelTextStyle,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        AuthTextField(textState = passwordText, onValueChange = { value -> passwordText.value = value}, isPasswordField = true )

        // errore riguardante password
        if(authViewModel.passwordErrorCode.value != -1) {
            ErrorLabel(authViewModel.passwordErrorCode.value)
        }

        Spacer(modifier = Modifier.weight(0.4f))



        // Login button
        AuthButton(
            text = LocalContext.current.getString(R.string.login),
            backgroundColor = MainGreen,
            onClick = {
                authViewModel.doCredentialsLogin(usernameText.value, passwordText.value)
            }
        )

        // errore di login generico
        if(authViewModel.genericErrorCode.value != -1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ErrorLabel(authViewModel.genericErrorCode.value)
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LocalContext.current.getString(R.string.forgot_password),
                color = WhiteDark2,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = LocalContext.current.getString(R.string.reset_password),
                color = WhiteColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.popBackStack(); navController.navigate(Screen.PasswordResetScreen.route); authViewModel.clearErrorMessages() }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // la riga che rimanda alla sign up page
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LocalContext.current.getString(R.string.dont_have_account),
                color = WhiteDark2,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = LocalContext.current.getString(R.string.signup),
                color = WhiteColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.popBackStack(); navController.navigate(Screen.SignUpScreen.route); authViewModel.clearErrorMessages() }
            )
        }
    }
}