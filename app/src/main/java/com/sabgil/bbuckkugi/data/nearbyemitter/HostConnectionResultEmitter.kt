package com.sabgil.bbuckkugi.data.nearbyemitter

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.common.ext.offerSuccess
import com.sabgil.bbuckkugi.data.model.Message
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class HostConnectionResultEmitter(
    private val endpointId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<Message>>
) {

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Timber.i("nearby: onPayloadReceived $endpointId, $payload")
            val receivedBytes = payload.asBytes() ?: return
            producerScope.offerSuccess(Message.fromBytes(receivedBytes))
        }

        override fun onPayloadTransferUpdate(
            endpointId: String,
            update: PayloadTransferUpdate
        ) {
            Timber.i("nearby: onPayloadTransferUpdate $endpointId, $update")
        }
    }

    fun emit() {
        connectionsClient.acceptConnection(
            endpointId,
            payloadCallback
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            producerScope.offerFailure(it)
            producerScope.close()
        }
    }
}