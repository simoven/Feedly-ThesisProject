package com.simoneventrici.feedly.presentation.navigation

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.simoneventrici.feedly.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PageSwiper(
    pagerState: PagerState,
    tabList: List<String>,
    padding: PaddingValues,
    allPages: Map<String, @Composable () -> Unit>,
    scrollUpState: State<Boolean?>
) {
    val coroutineScreen = rememberCoroutineScope()
    val position by animateFloatAsState(if (scrollUpState.value == true) -150f else 0f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(padding)
            .graphicsLayer { translationY = position },
    ) {
        // tabRow è la riga che contiene la parte in alto delle tab, il pulsante cliccabile
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .height(38.dp)
                .background(Color.Transparent)
                .padding(horizontal = 10.dp),
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                        .width(0.dp)
                        .height(0.dp)
                )
            },
            divider = {}
        ) {
            tabList.forEachIndexed { index, title ->
                val isCurrent = pagerState.currentPage == index
                val color = remember {
                    Animatable(Color.Transparent)
                }

                LaunchedEffect(key1 = pagerState.currentPage) {
                    color.animateTo(if (isCurrent) MainGreen else Color.Transparent)
                }

                Tab(
                    text = {
                        Text(
                            text = title,
                            color = if (isCurrent) WhiteColor else WhiteDark1,
                            fontSize = if (isCurrent) 16.sp else 14.sp,
                            fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Medium
                        )
                    },
                    selected = pagerState.currentPage == index,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color.value),
                    onClick = {
                        coroutineScreen.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        // L'horizontalPages fornisce un layout scrollabile all'interno del quale mettere le pagine
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            allPages[tabList[it].replaceFirstChar { ch -> ch.lowercase() }]?.invoke() ?: {}.invoke()
        }
    }
}