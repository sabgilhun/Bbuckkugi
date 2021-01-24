package com.sabgil.bbuckkugi.ui.receive

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupChannel()
    }

    private fun setupChannel() {
        communicationChannel.registerClient(this) {
            when (it) {
                is Data.Success -> binding.receiveText.text = it.data.byteValue.toString()
                is Data.Failure -> finish() // TODO error popup
            }
        }
    }

    companion object {
        fun start(context: Context, message: Message) {
            context.startActivity(
                Intent(context, ReceiveActivity::class.java).apply { putExtra("message", message) }
            )
        }
    }
}