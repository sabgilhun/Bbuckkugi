package com.sabgil.bbuckkugi.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnTop
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityHomeBinding
import com.sabgil.bbuckkugi.presentation.ui.discovery.DiscoveryActivity
import com.sabgil.bbuckkugi.presentation.ui.ladder.LadderActivity
import com.sabgil.bbuckkugi.service.ConnectionService
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeViewModel by viewModelOf()
    private val adapter = MessageLogAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = Handler()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter

        viewModel.loadMessages()
        startService(Intent(this, ConnectionService::class.java))

        viewModel.items.observeNonNull {
            adapter.replaceAll(it)
        }
    }

    inner class Handler {

        fun goToDiscovery() = DiscoveryActivity.start(this@HomeActivity)

        fun goToMoreMessage() {
            TODO()
        }

        fun goToLadderGame() = LadderActivity.start(this@HomeActivity)
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<HomeActivity>()
    }
}