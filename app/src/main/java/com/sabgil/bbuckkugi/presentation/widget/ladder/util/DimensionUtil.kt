package com.sabgil.bbuckkugi.presentation.widget.ladder.util

import android.content.res.Resources
import android.graphics.Rect
import android.util.DisplayMetrics

private val displayMetrics: DisplayMetrics by lazy { Resources.getSystem().displayMetrics }

/**
 * Returns boundary of the screen in pixels (px).
 */
val screenRectPx: Rect
    get() = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }

val screenWidth: Int
    get() = screenRectPx.width()