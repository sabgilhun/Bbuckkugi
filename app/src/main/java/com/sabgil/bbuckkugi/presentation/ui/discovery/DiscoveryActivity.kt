package com.sabgil.bbuckkugi.presentation.ui.discovery

import android.content.Context
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startWith
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityDiscoveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoveryActivity : BaseActivity<ActivityDiscoveryBinding>(R.layout.activity_discovery) {

    private val handler = Handler()
    private val viewModel by viewModelOf<DiscoveryViewModel>()
    private val adapter by lazy { DiscoveryAdapter(this, handler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.handler = handler
        setupViews()
        setupObserver()

        viewModel.startDiscovery()
    }

    private fun setupViews() {
        binding.discoveredRemoteRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.discoveredList.observeNonNull {
            adapter.update(it)
        }
    }

//    private fun setupChannel() {
//        discoveryChannel.registerClient(this) {
//            when (it) {
//                is Data.Success -> viewModel.discoveryRemote(it.data)
//                is Data.Failure -> showErrorMessage(it.exception.message.orEmpty())
//            }
//        }
//
//        connectionRequestChannel.registerClient(this) {
//            when (it) {
//                is Data.Success -> SendActivity.startOnHome(this)
//                is Data.Failure -> finish() // TODO error popup
//            }
//        }
//    }

    inner class Handler {

        fun activityFinish() = finish()

        fun connectEndpoint(endpoint: DiscoveryItem.Endpoint) {
            TODO()
        }
    }

    companion object {

        fun start(context: Context) = context.startWith<DiscoveryActivity>()
    }
}