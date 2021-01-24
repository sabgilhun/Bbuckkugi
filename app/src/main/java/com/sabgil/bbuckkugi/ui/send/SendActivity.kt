package com.sabgil.bbuckkugi.ui.send

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.databinding.ActivitySendBinding
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.service.channel.CommunicationChannel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendActivity : BaseActivity<ActivitySendBinding>(R.layout.activity_send) {

    @Inject
    lateinit var communicationChannel: CommunicationChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            button1.setOnClickListener {
                communicationChannel.sendTxData(Data.Message(0))
            }
            button2.setOnClickListener {
                communicationChannel.sendTxData(Data.Accept)
            }
            button3.setOnClickListener {
                communicationChannel.sendTxData(Data.Reject)
            }
            button4.setOnClickListener {
                communicationChannel.sendTxData(Data.Progress(0x04))
            }
            button5.setOnClickListener {
                communicationChannel.sendTxData(Data.Progress(0x08))
            }
        }
    }

    companion object {
        fun startOnHome(context: Context) {
            TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(Intent(context, SendActivity::class.java))
                .startActivities()
        }
    }
}