package com.sabgil.bbuckkugi.presentation.ui.discovery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.ext.toMutableList
import com.sabgil.bbuckkugi.data.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.repository.ConnectionManager

class DiscoveryViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    private val _discoveredList = MutableLiveData<List<DiscoveryItem>>(listOf())
    val discoveredList: LiveData<List<DiscoveryItem>> get() = _discoveredList

    fun startDiscovery() {
        val old = _discoveredList.value
        if (old.isNullOrEmpty()) {
            _discoveredList.value = listOf(DiscoveryItem.Header)
        }

        connectionManager.startDiscovery()
            .collectResult {
                success {
                    updateList(it)
                }
                error {
                    showErrorMessage(it)
                }
            }
    }

    private fun updateList(discoveredEndpoint: DiscoveredEndpoint) {
        val old = _discoveredList.value.toMutableList()
        val parsedData = discoveredEndpoint.endpointName.split("|")

        if (parsedData.size >= 2) {
            val name = parsedData[0]
            val gender = Gender.from(parsedData[1]) ?: return
            val profileImageUrl = if (parsedData.size == 2) null else parsedData[2]

            val item = DiscoveryItem.Endpoint(
                endpointId = discoveredEndpoint.endpointId,
                name = name,
                gender = gender,
                profileImageUrl = profileImageUrl
            )

            val indexOfAlreadyExist = old
                .filterIsInstance<DiscoveryItem.Endpoint>()
                .indexOfFirst { it.endpointId == discoveredEndpoint.endpointId }

            if (indexOfAlreadyExist == -1) {
                _discoveredList.value = old + item
            } else {
                old[indexOfAlreadyExist] = item
                _discoveredList.value = old
            }
        }
    }
}