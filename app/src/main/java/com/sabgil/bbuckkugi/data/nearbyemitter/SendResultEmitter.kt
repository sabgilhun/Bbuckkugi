package com.sabgil.bbuckkugi.data.nearbyemitter

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerEmptySuccess
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.data.model.Message
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class SendResultEmitter(
    private val endpointId: String,
    private val message: Message,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<Nothing>>
) {

    fun emit() {
        connectionsClient.sendPayload(endpointId, Payload.fromBytes(message.byteValue))
            .addOnSuccessListener {
                Timber.i("nearby: addOnSuccessListener")
                producerScope.offerEmptySuccess()
                producerScope.close()
            }
            .addOnFailureListener {
                Timber.i("nearby: addOnFailureListener $it")
                producerScope.offerFailure(it)
                producerScope.close()
            }
    }
}