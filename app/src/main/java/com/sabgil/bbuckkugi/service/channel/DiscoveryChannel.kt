package com.sabgil.bbuckkugi.service.channel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import javax.inject.Inject

class DiscoveryChannel @Inject constructor(context: Context) : BaseChannel(context) {

    fun registerClient(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Data<DiscoveredEndpoint>) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, ACTION_DISCOVERED_RESULT) { _, intent ->
        val result = intent.getSerializableExtra(ACTION_DISCOVERED_RESULT_INTENT_TAG)
        @Suppress("UNCHECKED_CAST")
        onReceive(result as Data<DiscoveredEndpoint>)
    }

    fun registerHost(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Action) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, ACTION_DISCOVERY_CONTROL) { _, intent ->
        val result = intent.getSerializableExtra(ACTION_DISCOVERY_CONTROL_INTENT_TAG)
        onReceive(result as Action)
    }

    fun sendActionForStartDiscovery() =
        sendBroadCast(ACTION_DISCOVERY_CONTROL) {
            putExtra(ACTION_DISCOVERY_CONTROL_INTENT_TAG, Action.DISCOVERY_START)
        }

    fun sendActionForStopDiscovery() =
        sendBroadCast(ACTION_DISCOVERY_CONTROL) {
            putExtra(ACTION_DISCOVERY_CONTROL_INTENT_TAG, Action.DISCOVERY_STOP)
        }


    fun sendResult(data: Data<DiscoveredEndpoint>) =
        sendBroadCast(ACTION_DISCOVERED_RESULT) {
            putExtra(ACTION_DISCOVERED_RESULT_INTENT_TAG, data)
        }


    enum class Action {
        DISCOVERY_START, DISCOVERY_STOP
    }

    companion object {
        private const val ACTION_DISCOVERED_RESULT =
            "ACTION_DISCOVERED_RESULT"
        private const val ACTION_DISCOVERED_RESULT_INTENT_TAG =
            "ACTION_DISCOVERED_RESULT_INTENT_TAG"

        private const val ACTION_DISCOVERY_CONTROL =
            "ACTION_DISCOVERY_CONTROL"
        private const val ACTION_DISCOVERY_CONTROL_INTENT_TAG =
            "ACTION_DISCOVERY_CONTROL_INTENT_TAG"
    }
}