package com.sabgil.bbuckkugi.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import kotlin.reflect.KClass

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