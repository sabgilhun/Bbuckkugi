package com.sabgil.bbuckkugi.presentation.ui.reply

import android.app.Activity
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.MessageCardList
import com.sabgil.bbuckkugi.common.ext.startForResult
import com.sabgil.bbuckkugi.databinding.ActivityReplyBinding
import com.sabgil.bbuckkugi.service.ConnectionService
import com.sabgil.extra.extra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyActivity : BaseActivity<ActivityReplyBinding>(R.layout.activity_reply) {

    private val isAgreed: Boolean by extra()
    private val messageCardIndex: Int by extra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            handler = Handler()

            emojiTextView.text =
                if (isAgreed) "\uD83E\uDD73"
                else "\uD83D\uDE2D"

            titleTextView.text =
                if (isAgreed) "상대방이 수락했어요."
                else "상대방이 거절했어요."

            messageCardImageView.setImageResource(
                if (isAgreed) MessageCardList.acceptMessageCards[messageCardIndex]
                else MessageCardList.rejectMessageCard[messageCardIndex]
            )
        }
    }

    inner class Handler {

        fun activityFinish() {
            ConnectionService.service?.restartAdvertising()
            setResult(RESULT_OK)
            finish()
        }
    }

    companion object {

        fun startForResult(
            activity: Activity,
            requestCode: Int,
            isAgreed: Boolean,
            messageCardIndex: Int
        ) = activity.startForResult<ReplyActivity>(
            requestCode,
            "isAgreed" to isAgreed,
            "messageCardIndex" to messageCardIndex
        )
    }
}