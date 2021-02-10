package com.sabgil.bbuckkugi.presentation.ui.guide

import android.content.Context
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnTop
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityGuideBinding
import com.sabgil.bbuckkugi.presentation.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideActivity : BaseActivity<ActivityGuideBinding>(R.layout.activity_guide) {

    private val viewModel by viewModelOf<GuideViewModel>()
    private val handler = Handler()

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.changePage(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.handler = handler

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            titleTextView.text = "아래 내용을\n꼭 한번 확인해주세요. \uD83D\uDE4F\uD83C\uDFFB"
            guideViewPager.adapter = GuideAdapter(this@GuideActivity)
            guideViewPager.registerOnPageChangeCallback(onPageChangeCallback)
            TabLayoutMediator(guideViewPagerTabLayout, guideViewPager) { _, _ -> }.attach()
        }
    }

    inner class Handler {

        fun goToLoginActivity() {
            LoginActivity.startOnTop(this@GuideActivity)
        }
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<GuideActivity>()
    }
}