package com.sabgil.bbuckkugi.data.model

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionResolution

sealed class AdvertisingResult {

    data class ConnectionInitiated(
        val endpointId: String,
        val connectionInfo: ConnectionInfo
    ) : AdvertisingResult()

    data class ConnectionResult(
        val endpointId: String,
        val resolution: ConnectionResolution
    ) : AdvertisingResult()

    data class Disconnected(
        val endpointId: String
    ) : AdvertisingResult()
}