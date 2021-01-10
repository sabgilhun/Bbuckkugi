package com.sabgil.bbuckkugi.nearbycallback

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data
import kotlinx.coroutines.channels.ProducerScope

class HostPayloadCallback(
    private val producerScope: ProducerScope<Result<Data>>
) : PayloadCallback() {

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        // offer data
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

    }
}