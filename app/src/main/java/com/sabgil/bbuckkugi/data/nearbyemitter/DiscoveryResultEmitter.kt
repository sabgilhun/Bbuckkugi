package com.sabgil.bbuckkugi.data.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.common.ext.offerSuccess
import com.sabgil.bbuckkugi.data.model.DiscoveredEndpoint
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class DiscoveryResultEmitter(
    private val serviceId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Data<DiscoveredEndpoint>>
) {

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Timber.i("nearby: onEndpointFound $endpointId, $info")
            producerScope.offerSuccess(
                DiscoveredEndpoint(
                    endpointId,
                    info.endpointName,
                    info.serviceId
                )
            )
        }

        override fun onEndpointLost(endpointId: String) {
            Timber.i("nearby: onEndpointLost $endpointId")
        }
    }

    fun emit() {
        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            discoveryOptions
        ).addOnSuccessListener {
            Timber.i("nearby: addOnSuccessListener")
        }.addOnFailureListener {
            Timber.i("nearby: addOnFailureListener $it")
            producerScope.offerFailure(it)
            producerScope.close()
        }
    }

    companion object {

        private val discoveryOptions = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_POINT_TO_POINT)
            .build()
    }
}