package com.sabgil.bbuckkugi.service.channel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.sabgil.bbuckkugi.common.Result
import javax.inject.Inject

class ConnectionRequestChannel @Inject constructor(context: Context) : BaseChannel(context) {

    fun registerClient(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Result<Nothing>) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, RECEIVE_RESULT) { _, intent ->
        val result = intent.getSerializableExtra(RECEIVE_RESULT_INTENT_TAG)
        @Suppress("UNCHECKED_CAST")
        onReceive(result as Result<Nothing>)
    }

    fun registerHost(
        lifecycleOwner: LifecycleOwner,
        onReceive: (String) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, RECEIVE_ACTION) { _, intent ->
        val endpoint = requireNotNull(intent.getStringExtra(RECEIVE_ACTION_INTENT_TAG))
        onReceive(endpoint)
    }

    fun sendActionForConnection(endpointId: String) =
        sendBroadCast(RECEIVE_ACTION) {
            putExtra(RECEIVE_ACTION_INTENT_TAG, endpointId)
        }

    fun sendResult(result: Result<Nothing>) =
        sendBroadCast(RECEIVE_RESULT) {
            putExtra(RECEIVE_RESULT_INTENT_TAG, result)
        }

    companion object {
        private const val RECEIVE_RESULT = "RECEIVE_RESULT"
        private const val RECEIVE_RESULT_INTENT_TAG = "RECEIVE_RESULT_INTENT_TAG"

        private const val RECEIVE_ACTION = "RECEIVE_ACTION"
        private const val RECEIVE_ACTION_INTENT_TAG = "RECEIVE_ACTION_INTENT_TAG"
    }
}