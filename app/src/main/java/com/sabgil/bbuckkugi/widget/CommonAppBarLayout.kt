package com.sabgil.bbuckkugi.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.ext.dpToPx
import com.sabgil.bbuckkugi.common.ext.layoutInflater
import com.sabgil.bbuckkugi.databinding.WidgetCommonAppBarBinding

class CommonAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = DataBindingUtil.inflate<WidgetCommonAppBarBinding>(
        context.layoutInflater,
        R.layout.widget_common_app_bar,
        this,
        true
    )

    init {
        readAttrs(attrs)
    }

    private fun readAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val array = context.obtainStyledAttributes(attrs, R.styleable.CommonAppBarLayout)

        val title = array.getString(R.styleable.CommonAppBarLayout_title).orEmpty()

        val isShowLeftButton =
            array.getBoolean(R.styleable.CommonAppBarLayout_showLeftButton, false)
        val leftButtonSrc = array.getDrawable(R.styleable.CommonAppBarLayout_leftButtonSrc)

        val isShowRightButton =
            array.getBoolean(R.styleable.CommonAppBarLayout_showRightButton, false)
        val rightButtonSrc = array.getDrawable(R.styleable.CommonAppBarLayout_rightButtonSrc)

        with(binding) {
            titleTextView.text = title

            leftButton.isInvisible = !isShowLeftButton
            leftButtonSrc?.let { leftButton.setImageDrawable(it) }

            rightButton.isInvisible = !isShowRightButton
            rightButtonSrc?.let { rightButton.setImageDrawable(it) }


        }

        elevation = context.dpToPx(8)
        background = AppCompatResources.getDrawable(context, R.color.white)

        array.recycle()
    }

    fun setOnClickLeftButton(onClickListener: OnClickListener) {
        binding.leftButton.setOnClickListener(onClickListener)
    }

    fun setOnClickRightButton(onClickListener: OnClickListener) {
        binding.rightButton.setOnClickListener(onClickListener)
    }
}