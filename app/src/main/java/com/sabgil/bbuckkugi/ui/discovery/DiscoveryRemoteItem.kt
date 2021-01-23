package com.sabgil.bbuckkugi.ui.discovery

import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.mutiviewtype.BaseItem

class DiscoveryRemoteItem(
    val endpointName: String,
    val endpointId: String
) : BaseItem(endpointId) {

    companion object {

        fun of(from: DiscoveredEndpoint) = DiscoveryRemoteItem(
            endpointName = from.endpointName,
            endpointId = from.endpointId
        )
    }
}