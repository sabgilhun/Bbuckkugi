package com.sabgil.bbuckkugi.presentation.widget.ladder

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import com.sabgil.bbuckkugi.common.ext.dpToPx
import com.sabgil.bbuckkugi.presentation.widget.ladder.util.screenWidth

class LadderScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : HorizontalScrollView(context, attrs, defStyleAttr), OnLadderScrollChangeListener {

    private val padding = context.dpToPx(48).toInt()
    var screenSize = 0

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.screenSize = screenWidth
    }

    override fun onScrollChange(x: Int) {
        val startGuide = scrollX + padding
        if (startGuide >= x) {
            scrollX = x - padding
            return
        }

        val endGuide = scrollX + screenSize - padding
        if (endGuide <= x) {
            // 카메라를 옮긴다는 생각으로 접근해야 함
            scrollX = x - screenSize + padding
            return
        }
    }
}