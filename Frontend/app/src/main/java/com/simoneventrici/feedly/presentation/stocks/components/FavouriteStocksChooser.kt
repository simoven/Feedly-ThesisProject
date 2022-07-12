package com.simoneventrici.feedly.presentation.stocks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.simoneventrici.feedly.model.Stock
import com.simoneventrici.feedly.presentation.stocks.StocksViewModel
import com.simoneventrici.feedly.ui.theme.*


@Composable
fun ChooseStockTopBar(
    navController: NavController,
    stockQueryText: MutableState<String>,
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
            Box(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow_icon),
                    modifier = Modifier
                        .size(32.dp),
                    contentDescription = "Back button",
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = LocalContext.current.getString(R.string.add_stock_label),
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
                    value = stockQueryText.value,
                    onValueChange = { text -> stockQueryText.value = text},
                    placeholder = {
                        Text(
                            text = LocalContext.current.getString(R.string.search_stock),
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
                    )
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Cancel",
                    color = MainGreen,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { searchBoxActive.value = false; stockQueryText.value = "" }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun FavouriteStockChooser(
    stocksViewModel: StocksViewModel,
    allStocks: List<Stock>,
    navController: NavController
) {
    val textOfStockQuery = remember { mutableStateOf("") }
    val searchBoxActive = remember { mutableStateOf(false) }
    val stocksFiltered = allStocks
        .filter { it.name.contains(textOfStockQuery.value, ignoreCase = true) || it.ticker.contains(textOfStockQuery.value, ignoreCase = true) }
        .sortedBy { it.name.lowercase() }
    val stocksChoosen = remember { mutableListOf<Stock>()}

    Column(
        Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp)
    ) {
        ChooseStockTopBar(navController = navController, stockQueryText = textOfStockQuery, searchBoxActive = searchBoxActive)

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    stocksFiltered
                ) { idx, stock ->
                    // controllo se questa azione è stata aggiunta all'elenco delle azioni scelte
                    val checked = remember { mutableStateOf(stocksChoosen.find { it.ticker == stock.ticker } != null) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stock.name,
                            color = WhiteColor
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stock.ticker.uppercase(),
                            color = WhiteDark2
                        )
                        Spacer(Modifier.weight(1f))
                        Checkbox(
                            checked = checked.value,
                            onCheckedChange = {
                                checked.value = !checked.value
                                if(checked.value)
                                    stocksChoosen.add(stock)
                                else
                                    stocksChoosen.remove(stock)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MainGreen
                            )
                        )
                    }

                    if(idx == stocksFiltered.size - 1) {
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
                        stocksViewModel.addStocksToFavourite(stocksChoosen.map { it.ticker })
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