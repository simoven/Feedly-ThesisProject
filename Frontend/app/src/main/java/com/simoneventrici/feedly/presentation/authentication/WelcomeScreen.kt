package com.simoneventrici.feedly.presentation.authentication

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.authentication.components.AuthButton
import com.simoneventrici.feedly.presentation.authentication.components.GoogleButton
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun WelcomeScreen(
    navController: NavController,
    googleClient: GoogleSignInClient,
    authViewModel: AuthViewModel
) {
    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    authViewModel.handleGoogleLogin(task.result.idToken ?: "")
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.weight(0.8f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_logo),
                modifier = Modifier.size(256.dp),
                contentDescription = "welcome logo"
            )
        }

        Spacer(modifier = Modifier.weight(1.2f))

        Text(
            text = LocalContext.current.getString(R.string.welcome_on_board),
            fontSize = 24.sp,
            color = WhiteColor,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = LocalContext.current.getString(R.string.get_started_now),
            fontSize = 18.sp,
            color = WhiteDark2,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(0.5f))

        AuthButton(
            text = LocalContext.current.getString(R.string.login),
            backgroundColor = MainGreen,
            onClick = { navController.navigate(Screen.LoginScreen.route) }
        )
        Spacer(Modifier.height(20.dp))
        AuthButton(
            text = LocalContext.current.getString(R.string.signup),
            backgroundColor = LighterGray,
            onClick = { navController.navigate(Screen.SignUpScreen.route) }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier.weight(1f).height(1.dp).background(LighterGray)
            )
            Text(
                text = LocalContext.current.getString(R.string.or),
                color = LighterGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(
                modifier = Modifier.weight(1f).height(1.dp).background(LighterGray)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        GoogleButton(
            onClick = {
                startForResult.launch(googleClient.signInIntent)
            }
        )

        Spacer(modifier = Modifier.weight(0.8f))
    }
}