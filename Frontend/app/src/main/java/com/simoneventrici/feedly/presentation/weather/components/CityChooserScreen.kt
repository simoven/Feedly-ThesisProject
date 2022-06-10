package com.simoneventrici.feedly.presentation.weather.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.weather.WeatherViewModel
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun ChooserTopBar(
    cityQueryText: MutableState<String>,
    queryChanged: MutableState<Boolean>,
    onSearchClicked: () -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow_icon),
            contentDescription = "Back button",
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.popBackStack() }
        )

        Spacer(Modifier.width(10.dp))

        TextField(
            value = cityQueryText.value,
            onValueChange = { text -> cityQueryText.value = text; queryChanged.value = true },
            placeholder = {
                Text(
                    text = LocalContext.current.getString(R.string.search_city),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
            },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                textColor = WhiteColor,
                placeholderColor = LighterGray,
                backgroundColor = Color.Transparent,
                cursorColor = DarkGreen,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClicked() }
            )
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = LocalContext.current.getString(R.string.clear),
            color = MainGreen,
            fontSize = 16.sp,
            modifier = Modifier.clickable { cityQueryText.value = "" }
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun CityChooserScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel
) {
    val cityQueryText = remember { mutableStateOf("") }
    val queryResults = weatherViewModel.geoLocalizationInfoQueryResult.value
    val queryChanged = remember { mutableStateOf(false) }
    val searchClicked = {
        weatherViewModel.getGeolocalizationDataFromAddress( cityQueryText.value )
        queryChanged.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(
                top = getSystemStatusbarHeightInDp(LocalContext.current).dp,
                start = 20.dp,
                end = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChooserTopBar(cityQueryText = cityQueryText, navController = navController, queryChanged = queryChanged, onSearchClicked = searchClicked)

        Spacer(modifier = Modifier.height(10.dp))

        if(queryChanged.value) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable { searchClicked() }) {
                Image(
                    painter = painterResource(id = R.drawable.search_unchecked),
                    contentDescription = "Search icon",
                    modifier = Modifier.size(26.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = cityQueryText.value,
                    color = WhiteDark1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Spacer(Modifier.height(10.dp))
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            if(queryResults is DataState.Success) {
                queryResults.data?.forEach { geoInfo ->
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { weatherViewModel.saveGeolocalizationAddress(geoInfo); navController.popBackStack();cityQueryText.value = "" }
                    ) {
                        Text(
                            text = geoInfo.fullLabel,
                            color = WhiteColor,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Divider(Modifier.fillMaxWidth())
                }
            }

            if(queryResults is DataState.Loading) {
                Row(Modifier.padding(horizontal = 10.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = WhiteDark2, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = LocalContext.current.getString(R.string.searching),
                        color = WhiteColor,
                        fontSize = 18.sp
                    )
                }
            }
        }

    }
}