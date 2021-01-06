package com.sabgil.bbuckkugi.common.ext

import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater get() = requireNotNull(LayoutInflater.from(this))

fun Context.dpToPx(dp: Float) = dp * (resources.displayMetrics.densityDpi / 160f)

fun Context.pxToDp(px: Float) = px / (resources.displayMetrics.densityDpi / 160f)