package com.sabgil.bbuckkugi.presentation.ui.discovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.BACK_TO_HOME_REQUEST_CODE
import com.sabgil.bbuckkugi.common.ext.startWith
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityDiscoveryBinding
import com.sabgil.bbuckkugi.presentation.ui.send.SendActivity
import com.sabgil.bbuckkugi.service.ConnectionService
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BACK_TO_HOME_REQUEST_CODE && resultCode == RESULT_OK) {
            ConnectionService.service?.restartAdvertising()
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun setupViews() {
        binding.discoveredRemoteRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.discoveredList.observeNonNull {
            adapter.update(it)
        }
    }

    inner class Handler {

        fun activityFinish() = finish()

        fun connectEndpoint(endpoint: DiscoveryItem.Endpoint) = SendActivity.startForResult(
            this@DiscoveryActivity,
            BACK_TO_HOME_REQUEST_CODE,
            endpoint.endpointId
        )
    }

    companion object {

        fun start(context: Context) = context.startWith<DiscoveryActivity>()
    }
}