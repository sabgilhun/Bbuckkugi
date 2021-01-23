package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

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
            Timber.i("nearby: onConnectionInitiated $endpointId, $connectionInfo")
            connectionClient.acceptConnection(endpointId, payloadCallback)
                .addOnFailureListener {
                    producerScope.offer(Result.Failure(it))
                    producerScope.close()
                }
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            Timber.i("nearby: onConnectionResult $endpointId, $resolution")
            if (resolution.status.statusCode != ConnectionsStatusCodes.STATUS_OK) {
                producerScope.close()
            }
        }

        override fun onDisconnected(endpointId: String) {
            Timber.i("nearby: onDisconnected $endpointId")
            producerScope.close()
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Timber.i("nearby: onPayloadReceived $endpointId, $payload")
            val receivedBytes = payload.asBytes() ?: return
            producerScope.offer(Result.Success(Data.fromBytes(receivedBytes)))
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Timber.i("nearby: onPayloadTransferUpdate $endpointId, $update")
        }
    }

    fun emit() {
        connectionClient.requestConnection(
            serviceId,
            endpointId,
            connectionLifecycleCallback
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
            producerScope.offer(Result.Success(Data.Start))
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            producerScope.offer(Result.Failure(it))
            producerScope.close()
        }
    }
}