package com.sabgil.bbuckkugi.presentation.ui.discovery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.model.DiscoveredEndpoint

class DiscoveryViewModel @ViewModelInject constructor() : BaseViewModel() {

    private val _discoveredList = MutableLiveData<List<DiscoveryItem>>(listOf())
    val discoveredList: LiveData<List<DiscoveryItem>> get() = _discoveredList

    fun discoveryRemote(discoveredEndpoint: DiscoveredEndpoint) {

    }

    fun startDiscovery() {
        val old = _discoveredList.value
        if (old.isNullOrEmpty()) {
            _discoveredList.value = listOf(DiscoveryItem.Header)
        }
    }
}