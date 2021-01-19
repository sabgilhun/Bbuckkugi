package com.sabgil.bbuckkugi.nearbyemitter

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import kotlinx.coroutines.channels.ProducerScope
import timber.log.Timber

class DiscoveryResultEmitter(
    private val serviceId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Result<DiscoveredEndpoint>>
) {

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Timber.i("nearby: onEndpointFound $endpointId, $info")
            producerScope.offer(
                Result.Success(DiscoveredEndpoint(endpointId, info.endpointName, info.serviceId))
            )
        }

        override fun onEndpointLost(endpointId: String) {
            Timber.i("nearby: onEndpointLost $endpointId")
            producerScope.close()
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
            producerScope.offer(Result.Failure(it))
            producerScope.close()
        }
    }

    companion object {
        private val discoveryOptions = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_POINT_TO_POINT)
            .build()
    }
}