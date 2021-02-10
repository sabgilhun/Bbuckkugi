package com.sabgil.bbuckkugi.data.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.common.ext.offerSuccess
import com.sabgil.bbuckkugi.data.model.AdvertisingResult
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class AdvertisingResultEmitter(
    private val hostName: String,
    private val serviceId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<AdvertisingResult>>
) {
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo
        ) {
            Timber.i("nearby: onConnectionInitiated $endpointId, $connectionInfo")
            producerScope.offerSuccess(
                AdvertisingResult.ConnectionInitiated(endpointId, connectionInfo)
            )
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            Timber.i("nearby: onConnectionResult $endpointId, ${resolution.status}")
            producerScope.offerSuccess(
                AdvertisingResult.ConnectionResult(endpointId, resolution)
            )
        }

        override fun onDisconnected(endpointId: String) {
            Timber.i("nearby: onDisconnected $endpointId")
            producerScope.offerSuccess(
                AdvertisingResult.Disconnected(endpointId)
            )
        }
    }

    fun emit() {
        connectionsClient.startAdvertising(
            hostName,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            producerScope.offerFailure(it)
            producerScope.close()
        }
    }

    companion object {
        private val advertisingOptions = AdvertisingOptions
            .Builder()
            .setStrategy(Strategy.P2P_POINT_TO_POINT)
            .build()
    }
}