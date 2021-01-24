package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.common.ext.offerSuccess
import com.sabgil.bbuckkugi.model.ConnectionRequest
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class AdvertisingResultEmitter(
    private val hostName: String,
    private val serviceId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<ConnectionRequest>>
) {
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo
        ) {
            Timber.i("nearby: onConnectionInitiated $endpointId, $connectionInfo")
            producerScope.offerSuccess(ConnectionRequest(endpointId, connectionInfo.endpointName))
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