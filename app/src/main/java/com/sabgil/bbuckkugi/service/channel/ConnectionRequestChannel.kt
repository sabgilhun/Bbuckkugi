package com.sabgil.bbuckkugi.service.channel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.sabgil.bbuckkugi.common.Data
import javax.inject.Inject

class ConnectionRequestChannel @Inject constructor(context: Context) : BaseChannel(context) {

    fun registerClient(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Data<Nothing>) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, ACTION_CONNECTION_RESULT) { _, intent ->
        val result = intent.getSerializableExtra(ACTION_CONNECTION_RESULT_INTENT_TAG)
        @Suppress("UNCHECKED_CAST")
        onReceive(result as Data<Nothing>)
    }

    fun registerHost(
        lifecycleOwner: LifecycleOwner,
        onReceive: (String) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, ACTION_REQUEST_CONNECTION) { _, intent ->
        val endpoint = requireNotNull(intent.getStringExtra(ACTION_REQUEST_CONNECTION_INTENT_TAG))
        onReceive(endpoint)
    }

    fun sendActionForConnection(endpointId: String) =
        sendBroadCast(ACTION_REQUEST_CONNECTION) {
            putExtra(ACTION_REQUEST_CONNECTION_INTENT_TAG, endpointId)
        }

    fun sendResult(data: Data<Nothing>) =
        sendBroadCast(ACTION_CONNECTION_RESULT) {
            putExtra(ACTION_CONNECTION_RESULT_INTENT_TAG, data)
        }

    companion object {
        private const val ACTION_CONNECTION_RESULT =
            "ACTION_CONNECTION_RESULT"
        private const val ACTION_CONNECTION_RESULT_INTENT_TAG =
            "ACTION_CONNECTION_RESULT_INTENT_TAG"

        private const val ACTION_REQUEST_CONNECTION =
            "ACTION_REQUEST_CONNECTION"
        private const val ACTION_REQUEST_CONNECTION_INTENT_TAG =
            "ACTION_REQUEST_CONNECTION_INTENT_TAG"
    }
}