package com.github.dyvoker.throbber

import android.util.DisplayMetrics

/**
 * Special converter for Density Independence Pixels.
 */
class DensityConverter(private val displayMetrics: DisplayMetrics) {

    fun dpToPx(dp: Float): Float = dp * displayMetrics.density

    fun pxToDp(px: Float): Float = px / displayMetrics.density
}