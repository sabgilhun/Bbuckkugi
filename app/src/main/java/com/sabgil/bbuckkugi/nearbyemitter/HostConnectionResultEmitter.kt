package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class HostConnectionResultEmitter(
    private val endpointId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Result<Data>>
) {

    private val payloadCallback = object : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Timber.i("nearby: onPayloadReceived $endpointId, $payload")
            val receivedBytes = payload.asBytes() ?: return
            producerScope.offer(Result.Success(Data.fromBytes(receivedBytes)))
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Timber.i("nearby: onPayloadReceived $endpointId, $update")
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
            producerScope.offer(Result.Failure(it))
            producerScope.close()
        }
    }
}