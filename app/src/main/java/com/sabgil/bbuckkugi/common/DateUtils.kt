@file:Suppress("FunctionName")

package com.sabgil.bbuckkugi.common

import java.text.SimpleDateFormat
import java.util.*

private val yy_MM_dd_HH_mm_format = SimpleDateFormat("yy.MM.dd HH:mm", Locale.KOREA)

fun Long.yy_MM_dd_HH_mm(): String = yy_MM_dd_HH_mm_format.format(Date(this * 1000))