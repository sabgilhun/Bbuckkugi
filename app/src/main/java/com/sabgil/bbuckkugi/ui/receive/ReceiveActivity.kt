package com.sabgil.bbuckkugi.ui.receive

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.databinding.ActivityReceiveBinding
import com.sabgil.bbuckkugi.model.Message
import com.sabgil.bbuckkugi.service.channel.CommunicationChannel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReceiveActivity : BaseActivity<ActivityReceiveBinding>(R.layout.activity_receive) {

    @Inject
    lateinit var communicationChannel: CommunicationChannel

    private val messageCards = listOf(
        R.drawable.message_card1,
        R.drawable.message_card2,
        R.drawable.message_card3,
        R.drawable.message_card4,
        R.drawable.message_card5,
        R.drawable.message_card6,
        R.drawable.message_card7,
        R.drawable.message_card8,
        R.drawable.message_card9,
        R.drawable.message_card10,
        R.drawable.message_card11,
        R.drawable.message_card12
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupChannel()
        startTimer()
    }

    private fun setupChannel() {
        communicationChannel.registerClient(this) {
            when (it) {
                is Data.Success -> {
                    val message = it.data
                    if (message is Message.MessageCard) {
                        val image =
                            ContextCompat.getDrawable(this, messageCards[message.messageCardIndex])
                        binding.imageView.setImageDrawable(image)
                    }
                }
                is Data.Failure -> finish() // TODO error popup
            }
        }
    }

    private fun startTimer() {
        object : CountDownTimer(TIMER_FINISH_DELAY, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.progress =
                    ((TIMER_FINISH_DELAY - millisUntilFinished) / COUNT_DOWN_INTERVAL).toInt()
            }

            override fun onFinish() {
                finish()
            }
        }.start()
    }

    companion object {
        private const val COUNT_DOWN_INTERVAL = 1000L
        private const val TIMER_FINISH_DELAY = 5 * 60 * COUNT_DOWN_INTERVAL

        fun startOnHome(context: Context) {
            TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(Intent(context, ReceiveActivity::class.java))
                .startActivities()
        }
    }
}