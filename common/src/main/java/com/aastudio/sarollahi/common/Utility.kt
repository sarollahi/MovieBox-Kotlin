package com.aastudio.sarollahi.common

import android.content.Context

fun calculateNoOfColumns(
    context: Context,
    columnWidthDp: Float
): Int { // For example column_Width_dp=180
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
}