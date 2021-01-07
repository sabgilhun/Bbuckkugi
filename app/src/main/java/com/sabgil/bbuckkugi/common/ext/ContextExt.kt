package com.sabgil.bbuckkugi.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import kotlin.reflect.KClass

val Context.layoutInflater get() = requireNotNull(LayoutInflater.from(this))

fun Context.dpToPx(dp: Float) = dp * (resources.displayMetrics.densityDpi / 160f)

fun Context.pxToDp(px: Float) = px / (resources.displayMetrics.densityDpi / 160f)

fun Context.startWith(target: KClass<out Activity>, vararg pairs: Pair<String, Any?>) {
    startActivity(Intent(this, target.java).apply {
        putExtras(bundleOf(*pairs))
    })
}

fun Context.startOnTop(target: KClass<out Activity>) {
    startActivity(Intent(this, target.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}