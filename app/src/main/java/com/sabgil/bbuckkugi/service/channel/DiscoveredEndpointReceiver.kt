package com.sabgil.bbuckkugi.service.channel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.service.ConnectionService.Companion.DISCOVERED_ENDPOINT

class DiscoveredEndpointReceiver(
    private val onReceive: (DiscoveredEndpoint) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DISCOVERED_ENDPOINT) {
            val discoveredEndpoint =
                intent.getSerializableExtra("discoveredEndpoint") as DiscoveredEndpoint
            onReceive(discoveredEndpoint)
        }
    }

    companion object {

        fun register(context: Context, onReceive: (DiscoveredEndpoint) -> Unit): BroadcastReceiver {
            val receiver = DiscoveredEndpointReceiver(onReceive)
            LocalBroadcastManager.getInstance(context)
                .registerReceiver(
                    receiver,
                    IntentFilter().apply {
                        addAction(DISCOVERED_ENDPOINT)
                    }
                )
            return receiver
        }
    }
}