package com.sabgil.bbuckkugi.ui.discovery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityDiscoveryBinding
import com.sabgil.bbuckkugi.databinding.ItemDiscoveryRemoteBinding
import com.sabgil.bbuckkugi.receiver.DiscoveredEndpointReceiver
import com.sabgil.bbuckkugi.service.ConnectionService
import com.sabgil.mutiviewtype.multiViewTypeAdapter
import com.sabgil.mutiviewtype.type
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoveryActivity : BaseActivity<ActivityDiscoveryBinding>(R.layout.activity_discovery) {

    private val viewModel by viewModelOf<DiscoveryViewModel>()

    private val adapter by lazy {
        multiViewTypeAdapter {
            type<DiscoveryRemoteItem, ItemDiscoveryRemoteBinding> {
                onCreate { binding, _, itemSupplier ->
                    binding.nameTextView.setOnClickListener {
                        val item = itemSupplier() ?: return@setOnClickListener
                        viewModel.connectRemote(item.endpointId)
                    }
                }

                onBind { item, binding ->
                    binding.item = item
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.discoveredRemoteRecyclerView.adapter = adapter

        setupObserver()
        registerReceiver()
        ConnectionService.sendStartDiscoveringAction(this)
    }

    private fun setupObserver() {
        viewModel.discoveredRemotes.observeNonNull {
            adapter.update(it)
        }
    }

    private fun registerReceiver() {
        val receiver = DiscoveredEndpointReceiver {
            viewModel.discoveryRemote(it)
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                receiver,
                IntentFilter().apply {
                    addAction(ConnectionService.DISCOVERED_ENDPOINT)
                }
            )
    }

    companion object {

        fun start(context: Context) = context.startActivity(
            Intent(context, DiscoveryActivity::class.java)
        )
    }
}