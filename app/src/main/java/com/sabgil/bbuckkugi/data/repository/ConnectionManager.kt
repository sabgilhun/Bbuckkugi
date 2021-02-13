package com.sabgil.bbuckkugi.data.repository

import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.data.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ConnectionManager {

    fun startAdvertising(hostName: String): Flow<Data<String>>

    fun startDiscovery(): Flow<Data<DiscoveredEndpoint>>

    fun connectRemote(endpointId: String): Flow<Data<Message>>

    fun acceptRemote(endpointId: String): Flow<Data<Message>>

    fun sendMessage(endpointId: String, message: Message): Flow<Data<Nothing>>
}