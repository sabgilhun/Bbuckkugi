package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerSuccess
import com.sabgil.bbuckkugi.model.Message
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.isActive
import timber.log.Timber

class PayloadEmitter : PayloadCallback() {

    var producerScope: ProducerScope<Data<Message>>? = null

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        Timber.i("nearby: onPayloadReceived $endpointId, $payload")
        val receivedBytes = payload.asBytes() ?: return
        val scope = producerScope
        if (scope?.isActive == true) {
            scope.offerSuccess(Message.fromBytes(receivedBytes))
        }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
        Timber.i("nearby: onPayloadTransferUpdate $endpointId, $update")
    }
}