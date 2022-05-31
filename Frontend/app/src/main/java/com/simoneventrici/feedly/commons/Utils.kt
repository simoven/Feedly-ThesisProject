package com.simoneventrici.feedly.commons

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.absoluteValue

fun getSystemStatusbarHeightInDp(context: Context): Int {
    val statusBarHeightId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = context.resources.getDimensionPixelSize(statusBarHeightId)
    return statusBarHeight / (context.resources.displayMetrics.densityDpi.absoluteValue / DisplayMetrics.DENSITY_DEFAULT)
}