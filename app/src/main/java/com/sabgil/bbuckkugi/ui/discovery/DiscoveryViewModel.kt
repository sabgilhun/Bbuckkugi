package com.sabgil.bbuckkugi.ui.discovery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.repository.ConnectionManager

class DiscoveryViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    private val _discoveredRemotes = MutableLiveData<List<DiscoveryRemoteItem>>(listOf())
    val discoveredRemotes: LiveData<List<DiscoveryRemoteItem>> get() = _discoveredRemotes

    fun discoveryRemote(discoveredEndpoint: DiscoveredEndpoint) {
        val oldList = requireNotNull(_discoveredRemotes.value)
        _discoveredRemotes.value =
            oldList + DiscoveryRemoteItem(
                discoveredEndpoint.endpointName,
                discoveredEndpoint.endpointId
            )
    }

    fun connectRemote(endpointId: String) {
        connectionManager.connectRemote(endpointId)
            .collectResult {
                success {
                    // TODO: 추가 처리 필요
                }
                error {
                    showErrorMessage(it)
                }
            }
    }

}