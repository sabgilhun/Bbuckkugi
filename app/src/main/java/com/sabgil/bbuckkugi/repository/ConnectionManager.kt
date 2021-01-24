package com.sabgil.bbuckkugi.repository

import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.model.AdvertisingResult
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.model.Message
import kotlinx.coroutines.flow.Flow

interface ConnectionManager {

    fun startAdvertising(hostName: String): Flow<Data<AdvertisingResult>>

    fun startDiscovery(): Flow<Data<DiscoveredEndpoint>>

    fun connectRemote(endpointId: String): Flow<Data<Nothing>>

    fun acceptRemote(endpointId: String): Flow<Data<Nothing>>

    fun listenMessage(): Flow<Data<Message>>

    fun sendMessage(endpointId: String, message: Message): Flow<Data<Nothing>>
}