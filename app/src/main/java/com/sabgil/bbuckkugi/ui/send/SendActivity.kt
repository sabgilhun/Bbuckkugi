package com.sabgil.bbuckkugi.ui.send

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.databinding.ActivitySendBinding
import com.sabgil.bbuckkugi.model.Message
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
            selectMessageCardViewPager.adapter = MessageCardAdapter(this@SendActivity).apply {
                notifyDataSetChanged()
            }
            messageCardSendButton.setOnClickListener {
                val index = selectMessageCardViewPager.currentItem
                communicationChannel.sendTxData(Message.MessageCard(index))
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