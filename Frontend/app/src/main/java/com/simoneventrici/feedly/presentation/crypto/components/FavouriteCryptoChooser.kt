package com.simoneventrici.feedly.presentation.crypto.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.presentation.crypto.CryptoViewModel
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun ChooseCryptoTopBar(
    navController: NavController,
    cryptoQueryText: MutableState<String>,
    searchBoxActive: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(
                if (!searchBoxActive.value) PaddingValues(
                    vertical = 10.dp,
                    horizontal = 15.dp
                ) else PaddingValues(start = 10.dp, end = 15.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(!searchBoxActive.value) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow_icon),
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.popBackStack() },
                contentDescription = "Back button",
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = LocalContext.current.getString(R.string.add_crypto_label),
                color = WhiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.search_unchecked),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { searchBoxActive.value = true },
                contentDescription = "Search icon"
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = cryptoQueryText.value,
                    onValueChange = { text -> cryptoQueryText.value = text},
                    //label = { Text(modifier = Modifier.height(0.dp), text = "")},
                    placeholder = {
                        Text(
                            text = LocalContext.current.getString(R.string.search_crypto),
                            fontSize = 16.sp,
                            fontWeight = W500
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
                    )
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Cancel",
                    color = MainGreen,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { searchBoxActive.value = false; cryptoQueryText.value = "" }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun FavouriteCryptoChooser(
    cryptoViewModel: CryptoViewModel,
    allCryptos: List<Crypto>,
    navController: NavController
) {
    val textOfCryptoQuery = remember { mutableStateOf("") }
    val searchBoxActive = remember { mutableStateOf(false) }
    val cryptos = allCryptos
        .filter { it.name.equals(textOfCryptoQuery.value, ignoreCase = true) || it.ticker.contains(textOfCryptoQuery.value, ignoreCase = true) }
        .sortedBy { it.name.lowercase() }
    val cryptoChoosen = remember { mutableListOf<Crypto>()}

    Column(
        Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp)
    ) {
        ChooseCryptoTopBar(navController = navController, cryptoQueryText = textOfCryptoQuery, searchBoxActive = searchBoxActive)

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    cryptos
                ) { idx, crypto ->
                    // controllo se questa crypto è stata aggiunta all'elenco delle crypto scelte
                    val checked = remember(cryptos.size) { mutableStateOf(cryptoChoosen.find { it.ticker == crypto.ticker } != null) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(crypto.imageUrl),
                            contentDescription = "crypto icon",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = crypto.name,
                            color = WhiteColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = crypto.ticker.uppercase(),
                            color = WhiteDark2
                        )
                        Spacer(Modifier.weight(1f))
                        Checkbox(
                            checked = checked.value,
                            onCheckedChange = {
                                checked.value = !checked.value
                                if(checked.value)
                                    cryptoChoosen.add(crypto)
                                else
                                    cryptoChoosen.remove(crypto)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MainGreen
                            )
                        )
                    }
                    
                    if(idx == cryptos.size - 1) {
                        // lo spacer serve altrimenti il FAB si sovrappone all'ultima checkbox impedendo di premerla
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }

            }

            Box(
                modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
            ) {
                FloatingActionButton(
                    backgroundColor = MainGreen,
                    onClick = {
                        cryptoViewModel.addCryptosToFavourite(cryptoChoosen)
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.check_icon),
                        contentDescription = "Confirm button",
                        tint = WhiteColor,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }

}