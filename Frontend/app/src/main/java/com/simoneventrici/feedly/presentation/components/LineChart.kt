package com.simoneventrici.feedly.presentation.components

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LineChart(
    values: List<Double>,
    modifier: Modifier,
    strokeColor: Color,
    textColor: Int = android.graphics.Color.WHITE,
    priceIndicators: Int = 5,
    showLabel: Boolean = true,
    strokeDp: Int = 3,
    offsetMultiplier: Float = 1f,
    showGradient: Boolean = true
) {
    val minPrice = values.minOfOrNull { it } ?: 0.0
    val maxPrice = values.maxOfOrNull { it} ?: 0.0

    val deltaPrice = (maxPrice - minPrice) / priceIndicators

    val density = LocalDensity.current
    val textPaint = remember {
        Paint().apply {
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(
        modifier = modifier,
    ) {
        val width = size.width
        val height = size.height
        // Gli spazi per le label
        val horOffset = (if (showLabel) 180f else 40f) * offsetMultiplier
        val vertOffset = 0f

        if(showLabel) {
            var currentPrice: Double = maxPrice
            // Divido l'altezza del canvas nei vari spazi per disegnare le label del prezzo
            val vertSpacer = height / (priceIndicators + 1)
            (0..priceIndicators).forEach {
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        String.format("$ %.2f", currentPrice),
                        50f,
                        60f + (vertSpacer * it),
                        textPaint
                    )
                }
                currentPrice -= deltaPrice
            }
        }

        var lastX = 0f
        // DrawWidth e DrawHeight sono le porzioni effettivamente disegnabili, escludendo lo spazio per le label
        val drawWidth = width - horOffset
        val drawHeight = height - vertOffset
        val priceDiff = maxPrice - minPrice

        val strokePath = androidx.compose.ui.graphics.Path().apply {
            values.indices.forEach() {
                val point1 = values[it]
                val point2 = values.getOrNull(it+1) ?: values.last()
                val idxPoint2 = if (it == values.size - 1) it else it + 1

                // Calcolo i punti di disegno, facendo la proporzione con i pixel disegnabili
                val x0 = (drawWidth * it / values.size) + horOffset
                val y0 = (maxPrice - point1) / priceDiff * drawHeight

                if(it == values.size - 1) {
                    lastX = x0
                }

                val x1 = (drawWidth * idxPoint2 / values.size) + horOffset
                val y1 = (maxPrice - point2) / priceDiff * drawHeight

                if(it == 0) {
                    moveTo(x0, y0.toFloat())
                }
                quadraticBezierTo(x0, y0.toFloat(), (x0 + x1) / 2, (y0.toFloat() + y1.toFloat()) / 2)
            }
        }

        // Creo il path per il linear gradient
        // Riprendo da dove è finito e vado verso sotto e poi a destra, definendo una figura
        val fillPath = Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, drawHeight)
                lineTo(horOffset, drawHeight)
                close()
            }

        drawPath(
            path = strokePath,
            color = strokeColor,
            style = Stroke(
                width = strokeDp.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        if(showGradient) {
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        strokeColor.copy(0.5f),
                        Color.Transparent
                    ),
                    endY = drawHeight
                )
            )
        }
    }
}