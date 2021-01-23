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
    ) = lifeCycleSafetyRegister(lifecycleOwner, ACTION_RX_DATA) { _, intent ->
        val result = intent.getSerializableExtra(ACTION_RX_DATA_INTENT_TAG)
        @Suppress("UNCHECKED_CAST")
        onReceive(result as Result<Data>)
    }

    fun registerHost(
        lifecycleOwner: LifecycleOwner,
        onReceive: (Data) -> Unit
    ) = lifeCycleSafetyRegister(lifecycleOwner, ACTION_TX_DATA) { _, intent ->
        val data = intent.getSerializableExtra(ACTION_TX_DATA_INTENT_TAG) as Data
        onReceive(data)
    }

    fun sendTxData(data: Data) =
        sendBroadCast(ACTION_TX_DATA) { putExtra(ACTION_TX_DATA_INTENT_TAG, data) }

    fun sendRxData(data: Result<Data>) =
        sendBroadCast(ACTION_RX_DATA) { putExtra(ACTION_RX_DATA_INTENT_TAG, data) }

    // TODO 연결 종료 기능 필요
    
    companion object {
        private const val ACTION_RX_DATA = "ACTION_RX_DATA"
        private const val ACTION_RX_DATA_INTENT_TAG = "ACTION_RX_DATA_INTENT_TAG"

        private const val ACTION_TX_DATA = "ACTION_TX_DATA"
        private const val ACTION_TX_DATA_INTENT_TAG = "ACTION_TX_DATA_INTENT_TAG"
    }
}