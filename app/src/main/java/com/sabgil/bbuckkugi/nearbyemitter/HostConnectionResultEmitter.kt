package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.ConnectionsClient
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerEmptySuccess
import com.sabgil.bbuckkugi.common.ext.offerFailure
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class HostConnectionResultEmitter(
    private val endpointId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<Nothing>>
) {
    private val payloadEmitter = PayloadEmitter()

    fun emit(): PayloadEmitter {
        connectionsClient.acceptConnection(
            endpointId,
            payloadEmitter
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
            producerScope.offerEmptySuccess()
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            producerScope.offerFailure(it)
            producerScope.close()
        }

        return payloadEmitter
    }
}