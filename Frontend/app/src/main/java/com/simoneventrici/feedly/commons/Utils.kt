package com.simoneventrici.feedly.commons

import android.content.Context
import android.util.DisplayMetrics
import java.text.NumberFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.pow

fun getSystemStatusbarHeightInDp(context: Context): Int {
    val statusBarHeightId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = context.resources.getDimensionPixelSize(statusBarHeightId)
    return statusBarHeight / (context.resources.displayMetrics.densityDpi.absoluteValue / DisplayMetrics.DENSITY_DEFAULT)
}

private fun getDecimalPlaces(number: Double): Int {
    var count = 0
    var numCpy = number
    while(numCpy < 1) {
        count++
        numCpy *= 10
    }

    return count
}

private fun getExp(number: Double): Int {
    var count = 0
    var numCpy = number
    while(numCpy > 1) {
        count++
        numCpy /= 10
    }

    return count
}

fun convertNumberInCurrency(number: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = max(2, getDecimalPlaces(number) + 2)
    format.currency = Currency.getInstance("USD")

    return format.format(number)
}

fun convertMarketCap(number: Double): String {
    val exp = getExp(number)
    if(exp < 7)
        return convertNumberInCurrency(number)
    if(exp in 7..9) {
        return String.format("%.2f M $", (number / 10.toDouble().pow(6)))
    }
    if(exp in 10..12) {
        return String.format("%.2f B $", (number / 10.toDouble().pow(9)))
    }
    if(exp in 13..15) {
        return String.format("%.2f T $", (number / 10.toDouble().pow(12)))
    }
    return ""
}