package com.sabgil.bbuckkugi.nearbycallback

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import kotlinx.coroutines.channels.ProducerScope

class DiscoveryCallback(
    private val producerScope: ProducerScope<Result<DiscoveredEndpoint>>
) : EndpointDiscoveryCallback() {

    override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
        producerScope.offer(
            Result.Success(DiscoveredEndpoint(endpointId, info.endpointName, info.serviceId))
        )
    }

    override fun onEndpointLost(endpointId: String) {
        producerScope.close()
    }
}