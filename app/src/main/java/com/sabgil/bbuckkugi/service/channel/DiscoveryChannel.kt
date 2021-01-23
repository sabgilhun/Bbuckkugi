package com.sabgil.bbuckkugi.service.channel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import javax.inject.Inject

class DiscoveryChannel @Inject constructor(context: Context) : BaseChannel(context) {

    fun registerClient(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Result<DiscoveredEndpoint>) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, RECEIVE_RESULT) { _, intent ->
        val result = intent.getSerializableExtra(RECEIVE_RESULT_INTENT_TAG)
        @Suppress("UNCHECKED_CAST")
        onReceive(result as Result<DiscoveredEndpoint>)
    }

    fun registerHost(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Action) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, RECEIVE_ACTION) { _, intent ->
        val result = intent.getSerializableExtra(RECEIVE_ACTION_INTENT_TAG)
        onReceive(result as Action)
    }

    fun sendActionForStartDiscovery() =
        sendBroadCast(RECEIVE_ACTION) {
            putExtra(RECEIVE_ACTION_INTENT_TAG, Action.DISCOVERY_START)
        }

    fun sendActionForStopDiscovery() =
        sendBroadCast(RECEIVE_ACTION) {
            putExtra(RECEIVE_ACTION_INTENT_TAG, Action.DISCOVERY_STOP)
        }


    fun sendResult(result: Result<DiscoveredEndpoint>) =
        sendBroadCast(RECEIVE_RESULT) {
            putExtra(RECEIVE_RESULT_INTENT_TAG, result)
        }


    enum class Action {
        DISCOVERY_START, DISCOVERY_STOP
    }

    companion object {
        private const val RECEIVE_RESULT = "RECEIVE_RESULT"
        private const val RECEIVE_RESULT_INTENT_TAG = "RECEIVE_RESULT_INTENT_TAG"

        private const val RECEIVE_ACTION = "RECEIVE_ACTION"
        private const val RECEIVE_ACTION_INTENT_TAG = "RECEIVE_ACTION_INTENT_TAG"
    }
}