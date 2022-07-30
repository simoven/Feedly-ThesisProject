package com.simoneventrici.feedly.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.model.Emoji
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.presentation.components.ShimmerEffectLoader
import com.simoneventrici.feedly.presentation.explore.components.NewsCard
import com.simoneventrici.feedly.presentation.explore.components.NewsLoader
import com.simoneventrici.feedly.presentation.explore.components.ScrollableTopBar
import com.simoneventrici.feedly.presentation.navigation.PageSwiper
import com.simoneventrici.feedly.ui.theme.DarkGreen
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.WhiteDark1

// questa classe contiene la pagina da dare allo swiper per ogni categoria di notizia
// la uso per evitare di ricomporre ogni volta la stessa pagina, controllando se lo stato era già success
data class PageState(
    val content: @Composable () -> Unit,
    val state: LoadingState,
    val onRefresh: () -> Unit
) {
    sealed class LoadingState {
        object Success : LoadingState()
        object Loading : LoadingState()
        object Error : LoadingState()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExploreScreen(
    newsViewModel: ExploreViewModel = hiltViewModel(),
    navController: NavController
) {
    val newsByCategoryState = newsViewModel.currentNewsByCategory

    val pagerState = rememberPagerState()
    val allCategory = NewsCategory.getAll()
    val currentCategory = allCategory[pagerState.currentPage]
    val scrollUpState = newsViewModel.scrollUp.observeAsState()
    val userToken = newsViewModel.userToken.value
    val isRefreshing = newsViewModel.isRefreshing.value

    val pagesMap = remember { mutableMapOf<String, PageState>() }

    // unico scrollState per tutte le pagine di caricamento
    val loadingPagesColumnScrollState = rememberScrollState()

    // se non ho le notizie di questa categoria, le fetcho
    if(newsByCategoryState.value[currentCategory.value] == null)
        newsViewModel.getNewsByCategory(userToken, currentCategory)

    // fetcho le notizie della categoria immediatamente successiva, per avere una transizione pulita nello swiper
    // se non è stato già richiesto il fetch per quella categoria9
    allCategory.getOrNull(pagerState.currentPage + 1)?.run {
        if(newsByCategoryState.value[this.value] == null) {
            newsViewModel.getNewsByCategory(userToken, allCategory[pagerState.currentPage + 1])
        }
    }

    // ogni volta che aumentano o diminuiscono le categorie in caricamento, creo le varie pagine e le inserisco in una mappa
    LaunchedEffect(key1 = newsByCategoryState.value.values.filter { it is DataState.Loading }.size) {
        // per ogni categoria, prendo il relativo oggetto stato
        newsByCategoryState.value.keys.forEach { categoryStr ->

            // se la categoria è stata fetchata con successo, mostro la relativa pagina
            when(val state = newsByCategoryState.value[categoryStr]) {
                is DataState.Success -> {
                    // se la pagina è già stata renderizzata, evito di farlo di nuovo
                    if(pagesMap[categoryStr]?.state is PageState.LoadingState.Success) return@forEach

                    val page = @Composable {
                        val idxEmojiBarOpen =  remember { mutableStateOf(-1) }
                        val columnScrollState = rememberLazyListState()

                        // serve solo per fare in modo che, se le reazioni cambiano, viene ricomposta la pagina
                        val latestNewsIdEmojiChanged = newsViewModel.latestNewsIdReactionAddedByCategory.value[categoryStr]

                        newsViewModel.updateScrollPosition(columnScrollState.firstVisibleItemIndex)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { idxEmojiBarOpen.value = -1 }
                                    )
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            state = columnScrollState
                        ) {
                            itemsIndexed(items = state.data ?: emptyList()) { idx, news ->
                                val openEmojiBar = { idxEmojiBarOpen.value = idx }
                                // funzione chiamata quando clicco un'emoji per aggiungere reazioni
                                val onEmojiClicked = { emoji: Emoji ->
                                    newsViewModel.addReactionToNews(
                                        category = categoryStr,
                                        authToken = userToken,
                                        newsId = news.news.id,
                                        emoji = emoji
                                    )
                                    idxEmojiBarOpen.value = -1
                                }

                                Spacer(modifier = Modifier.height(25.dp))
                                NewsCard(
                                    news = news.news,
                                    reactions = news.reactions,
                                    userReaction = news.userReaction,
                                    onLongClick = openEmojiBar,
                                    onEmojiClicked = onEmojiClicked,
                                    emojiBoxActive = idx == idxEmojiBarOpen.value
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
                    pagesMap[categoryStr] = PageState(
                        content = page,
                        state = PageState.LoadingState.Success,
                        onRefresh = { newsViewModel.refreshNews(NewsCategory.parse(categoryStr)) }
                    )
                }

                // pagina di errore
                is DataState.Error -> {
                    pagesMap[categoryStr] = PageState(
                        content = @Composable {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = state.errorMsg ?: LocalContext.current.getString(R.string.unexpected_error_msg),
                                        color = WhiteDark1,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        },
                        state = PageState.LoadingState.Error,
                        onRefresh = { newsViewModel.refreshNews(NewsCategory.parse(categoryStr)) }
                    )
                }

                // pagina di caricamento con shimmer effect
                is DataState.Loading -> {
                    pagesMap[categoryStr] = PageState(
                        content = @Composable {
                            ShimmerEffectLoader { brush ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Transparent)
                                        .verticalScroll(loadingPagesColumnScrollState),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    repeat(3) {
                                        NewsLoader(brush = brush)
                                    }
                                }
                            }
                        },
                        state = PageState.LoadingState.Loading,
                        onRefresh = { newsViewModel.isRefreshing.value = false }
                    )
                }
                else -> {}
            }
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
        ScrollableTopBar(scrollUpState, navController)

        // passo tutte le pagine allo swiper, che le renderizzerà in base alla tab attiva
        PageSwiper(
            pagerState = pagerState,
            tabList = allCategory.map { it.value.replaceFirstChar { ch -> ch.uppercase() } },
            padding = PaddingValues(top = 52.dp),
            allPages = pagesMap,
            scrollUpState = scrollUpState,
            isRefreshing = isRefreshing
        )

    }
}