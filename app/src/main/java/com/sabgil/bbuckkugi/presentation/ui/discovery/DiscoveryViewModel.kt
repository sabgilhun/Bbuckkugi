package com.sabgil.bbuckkugi.presentation.ui.discovery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.model.DiscoveredEndpoint

class DiscoveryViewModel @ViewModelInject constructor() : BaseViewModel() {

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
}