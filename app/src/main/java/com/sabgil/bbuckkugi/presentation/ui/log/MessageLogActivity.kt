package com.sabgil.bbuckkugi.presentation.ui.log

import android.content.Context
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startWith
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityMessageLogBinding
import com.sabgil.bbuckkugi.presentation.ui.home.MessageLogAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageLogActivity : BaseActivity<ActivityMessageLogBinding>(R.layout.activity_message_log) {

    private val viewModel by viewModelOf<MessageLogViewModel>()
    private val adapter = MessageLogAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.handler = Handler()
        binding.recyclerView.adapter = adapter

        viewModel.items.observeNonNull {
            adapter.replaceAll(it)
        }

        viewModel.loadMessages()
    }

    inner class Handler {
        fun finish() = this@MessageLogActivity.finish()
    }

    companion object {
        fun start(context: Context) = context.startWith<MessageLogActivity>()
    }
}