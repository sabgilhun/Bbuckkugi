package com.sabgil.bbuckkugi.repository

import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.model.ConnectionRequest
import com.sabgil.bbuckkugi.model.Message
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import kotlinx.coroutines.flow.Flow

interface ConnectionManager {

    fun startAdvertising(hostName: String): Flow<Data<ConnectionRequest>>

    fun startDiscovery(): Flow<Data<DiscoveredEndpoint>>

    fun connectRemote(endpointId: String): Flow<Data<Message>>

    fun acceptRemote(endpointId: String): Flow<Data<Message>>

    fun sendData(endpointId: String, message: Message): Flow<Data<Nothing>>
}