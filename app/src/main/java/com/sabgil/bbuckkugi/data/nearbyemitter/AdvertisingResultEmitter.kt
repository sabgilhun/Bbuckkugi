package com.sabgil.bbuckkugi.data.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.common.ext.offerSuccess
import com.sabgil.bbuckkugi.data.exception.ConnectionException
import com.sabgil.bbuckkugi.data.model.Message
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class AdvertisingResultEmitter(
    private val serviceId: String,
    private val connectionsClient: ConnectionsClient,
) {

    private var advertisingResultProducer: ProducerScope<Data<String>>? = null
    private var connectionResultProducer: ProducerScope<Data<Message>>? = null

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo
        ) {
            Timber.i("nearby: onConnectionInitiated $endpointId, $connectionInfo")
            advertisingResultProducer?.offerSuccess(endpointId)
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            Timber.i("nearby: onConnectionResult $endpointId, ${resolution.status}")
        }

        override fun onDisconnected(endpointId: String) {
            Timber.i("nearby: onDisconnected $endpointId")
            connectionResultProducer?.offerFailure(ConnectionException())
            detachConnectionResultProducer()
        }
    }

    fun emit(hostName: String) {
        connectionsClient.startAdvertising(
            hostName,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            advertisingResultProducer?.offerFailure(it)
            advertisingResultProducer?.close()
        }
    }

    fun attachAdvertisingResultProducer(producerScope: ProducerScope<Data<String>>) {
        advertisingResultProducer = producerScope
    }

    fun detachAdvertisingResultProducer() {
        advertisingResultProducer?.close()
        advertisingResultProducer = null
    }

    fun attachConnectionResultProducer(producerScope: ProducerScope<Data<Message>>) {
        connectionResultProducer = producerScope
    }

    fun detachConnectionResultProducer() {
        connectionResultProducer?.close()
        connectionResultProducer = null
    }

    companion object {

        private val advertisingOptions = AdvertisingOptions
            .Builder()
            .setStrategy(Strategy.P2P_POINT_TO_POINT)
            .build()
    }
}