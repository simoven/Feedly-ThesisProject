package com.simoneventrici.feedly.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor
import kotlinx.coroutines.NonCancellable.cancel

fun languageToFullName(lang: String): String {
    return when(lang) {
        "en" -> "English"
        "it" -> "Italiano"
        else -> ""
    }
}

fun languageFullNameToShortName(lang: String): String {
    return when(lang) {
        "English" -> "en"
        "Italiano" -> "it"
        else -> ""
    }
}

@Composable
fun ChooseLanguageDialog(
    currentLanguage: String, // es. "en" o "it"
    closeDialog: () -> Unit,
    saveLanguage: (String) -> Unit,
    options: List<String> = listOf("English", "Italiano")
) {
    val selectedLang = remember(currentLanguage) { mutableStateOf(currentLanguage) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack.copy(.5f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(LighterGray)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.choose_language),
                    color = WhiteColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(15.dp))

                options.forEach { lang ->
                    val selected = languageToFullName(selectedLang.value) == lang

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            text = lang,
                            color = WhiteColor,
                            fontSize = 16.sp,
                        )
                        Spacer(Modifier.weight(1f))
                        RadioButton(
                            selected = selected,
                            onClick = { selectedLang.value = languageFullNameToShortName(lang) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MainGreen,
                            )
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = LocalContext.current.getString(R.string.cancel),
                        color = WhiteColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.clickable { closeDialog() }
                    )
                    Text(
                        text = LocalContext.current.getString(R.string.confirm),
                        color = MainGreen,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.clickable { saveLanguage(selectedLang.value); closeDialog() }
                    )
                }

            }
        }
    }
}