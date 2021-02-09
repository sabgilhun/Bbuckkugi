package com.sabgil.bbuckkugi.common.ext

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.sabgil.bbuckkugi.base.BaseActivity

val Context.layoutInflater get() = requireNotNull(LayoutInflater.from(this))

fun Context.dpToPx(dp: Float) = dp * (resources.displayMetrics.densityDpi / 160f)

fun Context.pxToDp(px: Float) = px / (resources.displayMetrics.densityDpi / 160f)

inline fun <reified T : BaseActivity<*>> Context.startWith(vararg pairs: Pair<String, Any?>) =
    startActivity(intentFor(T::class.java, *pairs))

inline fun <reified T : BaseActivity<*>> Context.startOnTop(vararg pairs: Pair<String, Any?>) =
    startActivity(
        intentFor(T::class.java, *pairs).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )

fun Context.intentFor(clazz: Class<*>, vararg pairs: Pair<String, Any?>) =
    Intent(this, clazz).apply { putExtras(bundleOf(*pairs)) }