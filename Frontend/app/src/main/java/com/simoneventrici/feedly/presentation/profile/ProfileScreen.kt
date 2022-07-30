package com.simoneventrici.feedly.presentation.profile

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.model.User
import com.simoneventrici.feedly.presentation.authentication.AuthViewModel
import com.simoneventrici.feedly.presentation.authentication.components.AuthButton
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun ProfileCircle(
    user: User?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Spacer(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(100))
                    .background(MainGreen)
            )

            Text(
                text = user?.username?.first()?.uppercase() ?: "U",
                color = WhiteColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = user?.username ?: "user",
            color = WhiteDark1,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SettingsTitle(
    text: String
) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = MainGreen,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    user: User?,
    currentNewsLanguage: String,
    saveLanguage: (String) -> Unit
) {
    val chooseLangDialogActive = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp + 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.profile),
                    color = WhiteColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ProfileCircle(user = user)
            Spacer(modifier = Modifier.height(20.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color(0xFF1D1D1D))
                .padding(horizontal = 20.dp, vertical = 15.dp),
            horizontalAlignment = Alignment.Start
        ) {
            SettingsTitle(text = LocalContext.current.getString(R.string.news_settings))
            Spacer(Modifier.height(10.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .clickable { chooseLangDialogActive.value = true }
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.language_region_interest),
                    color = WhiteColor,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = languageToFullName(currentNewsLanguage),
                    color = WhiteDark2,
                    fontSize = 15.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            SettingsTitle(text = LocalContext.current.getString(R.string.security))
            Spacer(Modifier.height(10.dp))
            Text(
                text = LocalContext.current.getString(R.string.change_password),
                color = WhiteColor,
                fontSize = 18.sp,
                modifier = Modifier.clickable { navController.navigate(Screen.PasswordChangeScreen.route) }
            )


            Spacer(Modifier.weight(1f))
            AuthButton(
                text = LocalContext.current.getString(R.string.logout),
                backgroundColor = ChartRed,
                onClick = { authViewModel.doLogout() }
            )
            Spacer(Modifier.height(20.dp))

        }
    }

    if(chooseLangDialogActive.value) {
        ChooseLanguageDialog(
            currentLanguage = currentNewsLanguage,
            closeDialog = { chooseLangDialogActive.value = false },
            saveLanguage = saveLanguage
        )
    }
}