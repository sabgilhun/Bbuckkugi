package com.sabgil.bbuckkugi.presentation.ui.send

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnHome
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivitySendBinding
import com.sabgil.extra.extra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendActivity : BaseActivity<ActivitySendBinding>(R.layout.activity_send) {

    private val endpointId: String by extra()

    private val viewModel by viewModelOf<SendViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        setupViews()
        viewModel.connect(endpointId)
    }

    private fun setupViews() {
        with(binding) {
            selectMessageCardViewPager.adapter = MessageCardAdapter(this@SendActivity).apply {
                notifyDataSetChanged()
            }

            TabLayoutMediator(
                messageViewPagerTabLayout,
                selectMessageCardViewPager
            ) { _, _ ->

            }.attach()

//            messageCardSendButton.setOnClickListener {
//                val index = selectMessageCardViewPager.currentItem
//            }
        }
    }

    inner class Handler {

        fun activityFinish() = finish()
    }

    companion object {

        fun startOnHome(context: Context, endpointId: String) =
            context.startOnHome<SendActivity>("endpointId" to endpointId)
    }
}