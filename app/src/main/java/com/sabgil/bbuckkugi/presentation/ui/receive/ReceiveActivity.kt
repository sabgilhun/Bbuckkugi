package com.sabgil.bbuckkugi.presentation.ui.receive

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.MessageCardList
import com.sabgil.bbuckkugi.common.ext.startOnHome
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityReceiveBinding
import com.sabgil.extra.extra
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReceiveActivity : BaseActivity<ActivityReceiveBinding>(R.layout.activity_receive) {

    private val endpointId: String by extra()
    private val viewModel by viewModelOf<ReceiveViewModel>()
    private val receiveCardList = MessageCardList.sendMessageCards

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = Handler()
        binding.viewModel = viewModel

        setupObservers()

        viewModel.acceptRemote(endpointId)
    }

    private fun setupObservers() {
        with(viewModel) {
            startTimer.observe {
                startTimer()
            }

            isReceived.observeNonNull {
                binding.cardImageView.setImageResource(receiveCardList[it.messageCardIndex])
            }
        }
    }

    private fun startTimer() {
        object : CountDownTimer(TIMER_FINISH_DELAY, COUNT_DOWN_INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                val percent = (TIMER_FINISH_DELAY - millisUntilFinished) / TIMER_FINISH_DELAY
                binding.progressBar.setProgressPercentage(percent.toDouble(), true)
            }

            override fun onFinish() {
                finish()
            }
            
        }.start()
    }

    inner class Handler {

        fun activityFinish() = finish()

        fun reply() {

        }
    }

    companion object {
        private const val COUNT_DOWN_INTERVAL = 100L
        private const val TIMER_FINISH_DELAY = 5 * 60 * (10 * COUNT_DOWN_INTERVAL)

        fun startOnHome(
            context: Context,
            endpointId: String
        ) = context.startOnHome<ReceiveActivity>("endpointId" to endpointId)
    }
}