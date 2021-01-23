package com.sabgil.bbuckkugi.service.channel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import javax.inject.Inject

class CommunicationChannel @Inject constructor(context: Context) : BaseChannel(context) {

    fun registerClient(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Result<Data>) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, RECEIVE_RESULT) { _, intent ->
        val result = intent.getSerializableExtra(RECEIVE_RESULT_INTENT_TAG)
        @Suppress("UNCHECKED_CAST")
        onReceive(result as Result<Data>)
    }

    fun registerHost(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Data) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, RECEIVE_ACTION) { _, intent ->
        val data = intent.getSerializableExtra(RECEIVE_ACTION_INTENT_TAG) as Data
        onReceive(data)
    }

    fun sendData(data: Data) =
        sendBroadCast(RECEIVE_ACTION) { putExtra(RECEIVE_ACTION_INTENT_TAG, data) }

    companion object {
        private const val RECEIVE_RESULT = "RECEIVE_RESULT"
        private const val RECEIVE_RESULT_INTENT_TAG = "RECEIVE_RESULT_INTENT_TAG"

        private const val RECEIVE_ACTION = "RECEIVE_ACTION"
        private const val RECEIVE_ACTION_INTENT_TAG = "RECEIVE_ACTION_INTENT_TAG"
    }
}