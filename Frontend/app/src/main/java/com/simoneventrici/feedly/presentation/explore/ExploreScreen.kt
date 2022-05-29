package com.simoneventrici.feedly.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simoneventrici.feedly.presentation.explore.components.NewsCard

@Composable
fun ExploreScreen(
    newsViewModel: ExploreViewModel = hiltViewModel()
) {
    val newsByCategoryState = newsViewModel.currentNewsByCategory
    val newsByKeywordState = newsViewModel.currentNewsByKeyword

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = newsByCategoryState.value.data ?: emptyList()) { news ->
            Spacer(modifier = Modifier.height(25.dp))
            NewsCard(news = news.news, reactions = news.reactions, userReaction = news.userReaction)
            Divider(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .height(1.dp),
                color = Color.DarkGray
            )
        }
    }
}