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
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ReceiveActivity : BaseActivity<ActivityReceiveBinding>(R.layout.activity_receive) {

    private val endpointId: String by extra()
    private val viewModel by viewModelOf<ReceiveViewModel>()

    private val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

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
                binding.cardImageView.setImageResource(
                    MessageCardList.sendMessageCards[it.messageCardIndex]
                )
            }

            replyDone.observe {
                finish()
            }
        }
    }

    private fun startTimer() {
        object : CountDownTimer(TIMER_FINISH_DELAY, COUNT_DOWN_INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                val percent =
                    (TIMER_FINISH_DELAY - millisUntilFinished) * 100.0 / TIMER_FINISH_DELAY
                val format = timeFormat.format(millisUntilFinished)

                binding.progressBar.setProgressPercentage(percent, true)
                binding.remainTimeTextView.text = format
            }

            override fun onFinish() {
                finish()
            }
        }.start()
    }

    inner class Handler {

        fun activityFinish() = finish()

        fun agreeMessage() = viewModel.replayMessage(endpointId, true)

        fun rejectMessage() = viewModel.replayMessage(endpointId, false)
    }

    companion object {

        private const val TIMER_SECOND = 5 * 60
        private const val COUNT_DOWN_INTERVAL = 100L
        private const val TIMER_FINISH_DELAY = TIMER_SECOND * (10 * COUNT_DOWN_INTERVAL)

        fun startOnHome(
            context: Context,
            endpointId: String
        ) = context.startOnHome<ReceiveActivity>("endpointId" to endpointId)
    }
}