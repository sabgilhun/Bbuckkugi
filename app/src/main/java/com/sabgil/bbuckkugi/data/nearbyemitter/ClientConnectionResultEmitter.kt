package com.sabgil.bbuckkugi.data.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerEmptySuccess
import com.sabgil.bbuckkugi.common.ext.offerFailure
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class ClientConnectionResultEmitter(
    private val endpointId: String,
    private val serviceId: String,
    private val connectionClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<Nothing>>
) {
    private val payloadEmitter = PayloadEmitter()

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo
        ) {
            Timber.i("nearby: onConnectionInitiated $endpointId, $connectionInfo")
            connectionClient.acceptConnection(endpointId, payloadEmitter)
                .addOnSuccessListener {
                    Timber.i("nearby: addOnSuccessListener")
                    producerScope.offerEmptySuccess()
                }
                .addOnFailureListener {
                    Timber.i("nearby: addOnFailureListener $it")
                    producerScope.offerFailure(it)
                    producerScope.close()
                }
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            Timber.i("nearby: onConnectionResult $endpointId, ${resolution.status}")
            if (resolution.status.statusCode != ConnectionsStatusCodes.STATUS_OK) {
                producerScope.close()
            }
        }

        override fun onDisconnected(endpointId: String) {
            Timber.i("nearby: onDisconnected $endpointId")
            producerScope.close()
        }
    }

    fun emit(): PayloadEmitter {
        connectionClient.requestConnection(
            serviceId,
            endpointId,
            connectionLifecycleCallback
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            producerScope.offerFailure(it)
            producerScope.close()
        }
        return payloadEmitter
    }
}