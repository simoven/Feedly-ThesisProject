package com.simoneventrici.feedly.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color

// Lo shimmerLoader prende come parametro un composable che ha bisogno del brush di caricamento
// restituisce un nuovo composable che chiama il parametro a cui viene aggiunto il brush
@Composable
fun ShimmerEffectLoader(Content: @Composable (Brush) -> Unit) {
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.2f)
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f,
        targetValue = 1600f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500, // duration for the animation
                easing = FastOutSlowInEasing,
                delayMillis = 600
            )
        )
    )

    val brush = linearGradient(
        colors = gradient,
        start = Offset(x = translateAnimation.value - 250f, y = 0f),
        end = Offset(x = translateAnimation.value, y = 50f)
    )

    Content(brush)
}