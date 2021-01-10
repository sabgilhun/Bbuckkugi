package com.sabgil.bbuckkugi.repository

import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.ConnectionRequest
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import kotlinx.coroutines.flow.Flow

interface ConnectionManager {

    fun startAdvertising(hostName: String): Flow<Result<ConnectionRequest>>

    fun startDiscovery(): Flow<Result<DiscoveredEndpoint>>

    fun connectRemote(clientName: String, endpointId: String): Flow<Result<Data>>

    fun acceptRemote(endpointId: String): Flow<Result<Data>>
}