package com.sabgil.bbuckkugi.presentation.widget.ladder

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.ext.dpToPx

class LadderDrawingTool(context: Context) {

    val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = context.dpToPx(20)
        strokeWidth = context.dpToPx(1)
    }

    val linePaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        alpha = 130
        style = Paint.Style.FILL
        textSize = context.dpToPx(20)
        strokeCap = Paint.Cap.ROUND
        strokeWidth = context.dpToPx(2)
    }

    val boxPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL

        strokeWidth = context.dpToPx(3)
        strokeCap = Paint.Cap.ROUND
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }

    val bridgePaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        strokeWidth = context.dpToPx(4)
    }

    private val colorList = createColorList(context)

    val defaultColor = ContextCompat.getColor(context, R.color.grey_600)

    @ColorInt
    fun getColor(index: Int): Int {
        return colorList.getOrElse(index, defaultValue = { defaultColor })
    }

    private fun createColorList(context: Context): List<Int> {
        val colorList = mutableListOf<Int>()
        val colorArray = context.resources.obtainTypedArray(R.array.color_array)

        for (i in 0 until colorArray.length()) {
            val color = colorArray.getColor(i, 0)
            colorList.add(color)
        }

        colorArray.recycle()

        return colorList
    }
}