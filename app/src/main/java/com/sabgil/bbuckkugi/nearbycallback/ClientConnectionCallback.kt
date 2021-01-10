package com.sabgil.bbuckkugi.nearbycallback

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import kotlinx.coroutines.channels.ProducerScope

class ClientConnectionCallback(
    private val connectionClient: ConnectionsClient,
    private val producerScope: ProducerScope<Result<Data>>
) : ConnectionLifecycleCallback() {

    override fun onConnectionInitiated(
        endpointId: String,
        connectionInfo: ConnectionInfo
    ) {
        connectionClient.acceptConnection(endpointId, ClientPayloadCallback(producerScope))
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

    private class ClientPayloadCallback(
        private val producerScope: ProducerScope<Result<Data>>
    ) : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receivedBytes = payload.asBytes() ?: return
            producerScope.offer(Result.Success(Data.fromBytes(receivedBytes)))
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

        }
    }
}