package com.sabgil.bbuckkugi.repository

import android.content.Context
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.ConnectionRequest
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectionManagerImpl @Inject constructor(context: Context) : ConnectionManager {

    override fun startAdvertise(hostName: String): Flow<Result<ConnectionRequest>> {
        TODO("Not yet implemented")
    }

    override fun startDiscovery(): Flow<Result<DiscoveredEndpoint>> {
        TODO("Not yet implemented")
    }

    override fun connectRemote(clientName: String, endpointId: String): Flow<Result<Data>> {
        TODO("Not yet implemented")
    }

    override fun acceptRemote(endpointId: String): Flow<Result<Data>> {
        TODO("Not yet implemented")
    }
}