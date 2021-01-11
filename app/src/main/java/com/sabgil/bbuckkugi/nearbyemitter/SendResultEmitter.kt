package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import kotlinx.coroutines.channels.ProducerScope

class SendResultEmitter(
    private val endpointId: String,
    private val data: Data,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Result<Nothing>>
) {

    fun emit() {
        connectionsClient.sendPayload(endpointId, Payload.fromBytes(data.byteValue))
            .addOnSuccessListener {
                producerScope.close()
            }
            .addOnFailureListener {
                producerScope.offer(Result.Failure(it))
                producerScope.close()
            }
    }
}