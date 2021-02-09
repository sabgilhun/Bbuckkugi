package com.sabgil.bbuckkugi.ui.guide

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnTop
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityGuideBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideActivity : BaseActivity<ActivityGuideBinding>(R.layout.activity_guide) {

    private val viewModel by viewModelOf<GuideViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            titleTextView.text = "아래 내용을\n꼭 한번 확인해주세요. \uD83D\uDE4F\uD83C\uDFFB"
            guideViewPager.adapter = GuideAdapter(this@GuideActivity)
            TabLayoutMediator(guideViewPagerTabLayout, guideViewPager) { _, _ -> }.attach()
        }
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<GuideActivity>()
    }
}