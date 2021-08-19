package com.sabgil.bbuckkugi.presentation.widget.ladder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.RectF
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener

class AnimationManager(
    private val animationListener: AnimationListener,
) {
    var onLadderScrollChangeListener: OnLadderScrollChangeListener? = null

    private var animatorSetMap = hashMapOf<Int, AnimatorSet>()

    fun animate(column: Int, lineList: List<RectF>, scrollUpdate: Boolean) {
        animatorSetMap[column]?.run {
            removeAllListeners()
            cancel()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(createAnimatorList(column, lineList, scrollUpdate))
        animatorSet.addListener(
            onEnd = {
                animationListener.onAnimationComplete(column)
            },
            onCancel = {
                animationListener.onAnimationComplete(column)
            }
        )
        animatorSet.start()

        animatorSetMap[column] = animatorSet
    }

    fun isRunning(): Boolean {
        for (i in animatorSetMap.values.indices) {
            val position = getRunningAnimationPosition(i)
            if (position != VALUE_NONE) {
                return true
            }
        }
        return false
    }

    private fun createAnimatorList(column: Int, lineList: List<RectF>, scrollUpdate: Boolean): List<Animator> {
        return lineList.map { createAnimator(column, it, scrollUpdate) }
    }

    private fun createAnimator(column: Int, rect: RectF, scrollUpdate: Boolean): ValueAnimator {
        val propertyX =
            PropertyValuesHolder.ofInt(PROPERTY_X, rect.left.toInt(), rect.right.toInt())
        val propertyY =
            PropertyValuesHolder.ofInt(PROPERTY_Y, rect.top.toInt(), rect.bottom.toInt())

        return ValueAnimator().apply {
            setValues(propertyX, propertyY)
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { animator ->
                onAnimationUpdate(column, animator, scrollUpdate)
            }
        }
    }

    private fun onAnimationUpdate(column: Int, valueAnimator: ValueAnimator?, scrollUpdate: Boolean) {
        if (valueAnimator == null) return

        val x = valueAnimator.getAnimatedValue(PROPERTY_X) as? Int ?: 0
        val y = valueAnimator.getAnimatedValue(PROPERTY_Y) as? Int ?: 0

        val value = AnimationValue(x, y, getRunningAnimationPosition(column))

        animationListener.onAnimationUpdate(column, value)
        if (scrollUpdate) {
            onLadderScrollChangeListener?.onScrollChange(x)
        }
    }

    private fun getRunningAnimationPosition(column: Int): Int {
        val childAnimations = animatorSetMap[column]?.childAnimations ?: return VALUE_NONE
        for (i in childAnimations.indices) {
            val animator = childAnimations[i]
            if (animator.isRunning) {
                return i
            }
        }
        return VALUE_NONE
    }

    interface AnimationListener {
        fun onAnimationUpdate(column: Int, animationValue: AnimationValue)
        fun onAnimationComplete(column: Int)
    }

    companion object {
        private const val PROPERTY_X = "PROPERTY_X"
        private const val PROPERTY_Y = "PROPERTY_Y"
        private const val ANIMATION_DURATION = 150L
        const val VALUE_NONE = -1
    }
}