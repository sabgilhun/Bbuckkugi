package com.sabgil.bbuckkugi.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.service.ConnectionService.Companion.DISCOVERED_ENDPOINT

class DiscoveredEndpointReceiver(
    private val onDiscovered: (DiscoveredEndpoint) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DISCOVERED_ENDPOINT) {
            val discoveredEndpoint =
                intent.getSerializableExtra("discoveredEndpoint") as DiscoveredEndpoint
            onDiscovered(discoveredEndpoint)
        }
    }
}