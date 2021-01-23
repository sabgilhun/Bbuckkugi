package com.sabgil.bbuckkugi.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.service.ConnectionService.Companion.RECEIVED_DATA

class DataReceiver(
    private val onReceive: (Data) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == RECEIVED_DATA) {
            val discoveredEndpoint =
                intent.getSerializableExtra("data") as Data
            onReceive(discoveredEndpoint)
        }
    }

    companion object {

        fun register(context: Context, onReceive: (Data) -> Unit): BroadcastReceiver {
            val receiver = DataReceiver(onReceive)
            LocalBroadcastManager.getInstance(context)
                .registerReceiver(
                    receiver,
                    IntentFilter().apply {
                        addAction(RECEIVED_DATA)
                    }
                )
            return receiver
        }
    }
}