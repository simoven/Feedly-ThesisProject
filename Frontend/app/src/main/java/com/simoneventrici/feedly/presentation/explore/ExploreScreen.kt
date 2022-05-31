package com.simoneventrici.feedly.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.presentation.explore.components.NewsCard
import com.simoneventrici.feedly.presentation.explore.components.ScrollableTopBar
import com.simoneventrici.feedly.presentation.navigation.PageSwiper
import com.simoneventrici.feedly.ui.theme.DarkGreen
import com.simoneventrici.feedly.ui.theme.DarkGreen2
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.MainGreen


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExploreScreen(
    newsViewModel: ExploreViewModel = hiltViewModel()
) {
    val newsByCategoryState = newsViewModel.currentNewsByCategory
    val newsByKeywordState = newsViewModel.currentNewsByKeyword

    val pagerState = rememberPagerState()
    val allCategory = NewsCategory.getAll()
    val currentCategory = allCategory[pagerState.currentPage]
    val scrollUpState = newsViewModel.scrollUp.observeAsState()

    val pagesMap = remember { mutableMapOf<String, @Composable () -> Unit>() }

    // se non ho le notizie di questa categoria, le fetcho
    if(newsByCategoryState.value[currentCategory.value] == null)
        newsViewModel.getNewsByCategory(Constants.TEST_TOKEN, currentCategory, "en")

    // fetcho le notizie della categoria immediatamente successiva, per avere una transizione pulita nello swiper
    if(pagerState.currentPage < allCategory.size - 1) {
        if(newsByCategoryState.value[allCategory[pagerState.currentPage + 1].value] == null)
            newsViewModel.getNewsByCategory(Constants.TEST_TOKEN, allCategory[pagerState.currentPage + 1], "en")
    }

    // ogni volta che viene fetchata con successo una nuova categoria di notizie, creo le varie pagine e le inserisco in una mappa
    LaunchedEffect(key1 = newsByCategoryState.value.values.filter { it is DataState.Success }.size) {
        println("Cambiati i valori della mappa")
        newsByCategoryState.value.keys.forEach { key ->
            val state = newsByCategoryState.value[key]
            val page = @Composable {
                val columnScrollState = rememberLazyListState()
                newsViewModel.updateScrollPosition(columnScrollState.firstVisibleItemIndex)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = columnScrollState
                ) {
                    items(items = state?.data ?: emptyList()) { news ->
                        Spacer(modifier = Modifier.height(25.dp))
                        NewsCard(
                            news = news.news,
                            reactions = news.reactions,
                            userReaction = news.userReaction
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(.9f)
                                .height(1.dp),
                            color = Color.DarkGray
                        )
                    }
                }
            }
            pagesMap[key] = page
        }
    }

    val gradient = Brush.radialGradient(
        colors = listOf(
            DarkGreen,
            LighterBlack,
        ),
        center = Offset(x = -150f, y = -600f),
        radius = 1600f
    )

    // poichè la statusbar è trasparent, creo un box con il colore di background che prende tutto lo schermo
    Box(modifier = Modifier
        .fillMaxSize()
        .background(gradient))

    // questo secondo box ha un offset uguale all'altezza della statusbar, in modo da evitare sovrapposizioni con i notch
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp)
    ) {
        ScrollableTopBar(scrollUpState)

        // passo tutte le pagine allo swiper, che le renderizzerà in base alla tab attiva
        PageSwiper(
            pagerState = pagerState,
            tabList = allCategory.map { it.value.replaceFirstChar { ch -> ch.uppercase() } },
            padding = PaddingValues(top = 52.dp),
            allPages = pagesMap,
            scrollUpState = scrollUpState
        )

    }
}