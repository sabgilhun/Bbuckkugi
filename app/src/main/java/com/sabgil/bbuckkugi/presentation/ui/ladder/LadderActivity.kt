package com.sabgil.bbuckkugi.presentation.ui.ladder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.addListener
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startWith
import com.sabgil.bbuckkugi.databinding.ActivityLadderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LadderActivity : BaseActivity<ActivityLadderBinding>(R.layout.activity_ladder) {

    private val viewModel by viewModels<LadderViewModel>()

    private val handler = Handler()
    private val adapter: LadderAdapter by lazy { LadderAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = handler
        binding.vm = viewModel

        binding.rvLadder.adapter = adapter

        viewModel.playerResultList.observeNonNull {
            adapter.update(it)
        }

        viewModel.uiState.observeNonNull {
            when (it) {
                LadderViewModel.UiState.SETTING -> {
                    showGameSettingView()
                }
                LadderViewModel.UiState.RUNNING -> {
                    hideGameSettingView()
                    binding.ladderView.gameStart(viewModel.playerCount())
                }
            }
        }

        viewModel.gameFinishEvent.observeNonNull {
            binding.ladderView.animateAll()
        }
    }

    private fun showGameSettingView() {
        binding.groupLadderGame.visibility = View.INVISIBLE
        binding.clGameSetting.visibility = View.VISIBLE

        val alphaAnim = ObjectAnimator.ofFloat(binding.clGameSetting, "alpha", 0.7f, 1f).apply {
            duration = 500L
            interpolator = AccelerateDecelerateInterpolator()
        }

        val translationYAnim =
            ObjectAnimator.ofFloat(binding.clGameSetting, "translationY", -100f, 0f).apply {
                duration = 500L
                interpolator = AccelerateDecelerateInterpolator()
            }

        val set = AnimatorSet()
        set.playTogether(alphaAnim, translationYAnim)
        set.start()
    }

    private fun hideGameSettingView() {
        binding.groupLadderGame.visibility = View.VISIBLE

        val alphaAnim = ObjectAnimator.ofFloat(binding.clGameSetting, "alpha", 1f, 0f).apply {
            duration = 500L
            interpolator = AccelerateDecelerateInterpolator()
        }

        val translationYAnim =
            ObjectAnimator.ofFloat(binding.clGameSetting, "translationY", 0f, -200f).apply {
                duration = 500L
                interpolator = AccelerateDecelerateInterpolator()
            }
        val set = AnimatorSet()
        set.playTogether(alphaAnim, translationYAnim)
        set.addListener(onEnd = {
            binding.clGameSetting.visibility = View.INVISIBLE
        })
        set.start()
    }

    inner class Handler {
        fun activityFinish() = finish()
    }

    companion object {

        fun start(context: Context) = context.startWith<LadderActivity>()
    }
}