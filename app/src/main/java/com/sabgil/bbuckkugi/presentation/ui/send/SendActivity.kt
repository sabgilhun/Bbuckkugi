package com.sabgil.bbuckkugi.presentation.ui.send

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.BACK_TO_HOME_REQUEST_CODE
import com.sabgil.bbuckkugi.common.ext.startForResult
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivitySendBinding
import com.sabgil.bbuckkugi.presentation.ui.reply.ReplyActivity
import com.sabgil.extra.extra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendActivity : BaseActivity<ActivitySendBinding>(R.layout.activity_send) {

    private val endpointId: String by extra()
    private val viewModel by viewModelOf<SendViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = Handler()
        binding.viewModel = viewModel

        setupViews()
        viewModel.connect(endpointId)

        viewModel.receivedReply.observeNonNull {
            ReplyActivity.startForResult(
                this,
                BACK_TO_HOME_REQUEST_CODE,
                it.isAgreed,
                it.messageType
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BACK_TO_HOME_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
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
        }
    }

    inner class Handler {

        fun activityFinish() {
            setResult(RESULT_OK)
            finish()
        }

        fun sendMessage() {
            val index = binding.selectMessageCardViewPager.currentItem
            viewModel.sendMessage(endpointId, index)
        }
    }

    companion object {

        fun startForResult(activity: Activity, requestCode: Int, endpointId: String) =
            activity.startForResult<SendActivity>(requestCode, "endpointId" to endpointId)
    }
}