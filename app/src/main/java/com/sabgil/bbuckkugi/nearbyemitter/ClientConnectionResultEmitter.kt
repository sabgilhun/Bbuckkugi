package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import kotlinx.coroutines.channels.ProducerScope

class ClientConnectionResultEmitter(
    private val endpointId: String,
    private val serviceId: String,
    private val connectionClient: ConnectionsClient,
    private val producerScope: ProducerScope<Result<Data>>
) {

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo
        ) {
            connectionClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            if (resolution.status.statusCode != ConnectionsStatusCodes.STATUS_OK) {
                producerScope.close()
            }
        }

        override fun onDisconnected(endpointId: String) {
            producerScope.close()
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receivedBytes = payload.asBytes() ?: return
            producerScope.offer(Result.Success(Data.fromBytes(receivedBytes)))
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

        }
    }

    fun emit() {
        connectionClient.requestConnection(
            serviceId,
            endpointId,
            connectionLifecycleCallback
        ).addOnFailureListener {
            producerScope.offer(Result.Failure(it))
            producerScope.close()
        }
    }
}