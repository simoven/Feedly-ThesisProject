package com.simoneventrici.feedly.presentation.explore.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.simoneventrici.feedly.model.NewsAndReactions
import com.simoneventrici.feedly.presentation.explore.ExploreViewModel
import com.simoneventrici.feedly.ui.theme.*

@Composable
fun SearchTopBar(
    navController: NavController,
    newsQueryText: MutableState<String>,
    searchBoxOpen: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow_icon),
            contentDescription = "Back button",
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.popBackStack() }
        )

        Spacer(Modifier.width(5.dp))

        TextField(
            value = newsQueryText.value,
            onValueChange = { text -> newsQueryText.value = text.replace("\n", ""); searchBoxOpen.value = true },
            placeholder = {
                Text(
                    text = LocalContext.current.getString(R.string.search_by_keyword_source),
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = LocalContext.current.getString(R.string.close),
            color = MainGreen,
            fontSize = 16.sp,
            modifier = Modifier.clickable { searchBoxOpen.value = false }
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun SearchNewsPage(
    exploreViewModel: ExploreViewModel,
    navController: NavController,
) {
    val newsQueryText = remember { mutableStateOf("") }
    val searchBoxOpen = remember { mutableStateOf(false) }
    // prendo la lista di tutte le notizie, e la cambio se cambia il numero di state success, ovvero se ci sono altre notizie appena fetchate
    val allNewsList = remember(exploreViewModel.currentNewsByCategory.value.values.map { it is DataState.Success }.size) { exploreViewModel.getAllNews() }
    val queryedNews = remember { mutableStateOf<List<NewsAndReactions>>(emptyList()) }

    val searchBySourceClicked = {
        queryedNews.value = allNewsList.filter { newsAndReactions ->
            newsAndReactions.news.sourceId.contains(newsQueryText.value, ignoreCase = true) ||
            newsAndReactions.news.sourceName.contains(newsQueryText.value, ignoreCase = true)
        }
        searchBoxOpen.value = false
    }

    val searchByKeywordClicked = {
        queryedNews.value = allNewsList.filter { newsAndReactions ->
            newsAndReactions.news.title.contains(newsQueryText.value, ignoreCase = true) ||
            newsAndReactions.news.description.contains(newsQueryText.value, ignoreCase = true)
        }
        searchBoxOpen.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
            .padding(
                top = getSystemStatusbarHeightInDp(LocalContext.current).dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchTopBar(newsQueryText = newsQueryText, navController = navController, searchBoxOpen = searchBoxOpen)

        Spacer(modifier = Modifier.height(10.dp))

        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                queryedNews.value.forEach { news ->
                    Spacer(modifier = Modifier.height(25.dp))
                    NewsCard(
                        news = news.news,
                        reactions = news.reactions,
                        userReaction = news.userReaction,
                        onLongClick = { },
                        onEmojiClicked = { },
                        emojiBoxActive = false
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .height(1.dp),
                        color = Color.DarkGray
                    )
                }
            }


            // è il box dove appaiono i due pulsanti per cercare nelle keyword o nelle fonti
            if (searchBoxOpen.value) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp, horizontal = 15.dp)
                        .background(LighterBlack)
                ) {
                    val actions: Map<String, () -> Unit> = mapOf(
                        Pair(
                            LocalContext.current.getString(R.string.in_keywords),
                            searchByKeywordClicked
                        ),
                        Pair(
                            LocalContext.current.getString(R.string.in_sources),
                            searchBySourceClicked
                        ),
                    )

                    // la key sarà un valore del tipo "nelle keyword" e il valore sarà la funzione corrispondente
                    // quando viene cliccato il testo, viene avviata la ricerca per keyword tra tutte le notizie
                    actions.entries.forEach {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { it.value() }
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalAlignment = CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.search_unchecked),
                                contentDescription = "Search icon",
                                modifier = Modifier.size(26.dp)
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            Row(
                                verticalAlignment = Bottom
                            ) {
                                Text(
                                    text = newsQueryText.value,
                                    color = WhiteDark1,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = it.key,
                                    color = WhiteDark2,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}














