package com.sabgil.bbuckkugi.data.model

import java.io.Serializable

data class DiscoveredEndpoint(
    val endpointId: String,
    val endpointName: String,
    val serviceId: String
): Serializable