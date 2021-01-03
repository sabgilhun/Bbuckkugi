package com.sabgil.bbuckkugi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    private val serviceId = "My Unique value"
    private val nickname = "nickname"

    private var isHost = true

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Log.i(
                this@MainActivity.localClassName,
                "received:" + payload.asBytes()?.toString(Charset.defaultCharset())
            )
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

        }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, p1: ConnectionInfo) {
            Nearby.getConnectionsClient(this@MainActivity)
                .acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, p1: ConnectionResolution) {
            when (p1.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Log.i(this@MainActivity.localClassName, "connection success")
                    sendData(endpointId)
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    Log.i(this@MainActivity.localClassName, "connection rejected")
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                    Log.i(this@MainActivity.localClassName, "connection error")
                }
                else -> {
                    Log.i(this@MainActivity.localClassName, "connection illegal state")
                }
            }
        }

        override fun onDisconnected(p0: String) {
            Log.i(this@MainActivity.localClassName, "Disconnect")
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, p1: DiscoveredEndpointInfo) {
            Nearby.getConnectionsClient(this@MainActivity)
                .requestConnection(nickname, endpointId, connectionLifecycleCallback)
                .addOnSuccessListener {
                    Log.i(this@MainActivity.localClassName, "connection start")
                }
                .addOnFailureListener {
                    Log.i(this@MainActivity.localClassName, "connection fail", it)
                }
        }

        override fun onEndpointLost(p0: String) {
            Log.i(this@MainActivity.localClassName, "discover lost")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startAdvertising()

        findViewById<View>(R.id.button).setOnClickListener {
            isHost = false
            startDiscovery()
        }
    }

    private fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions
            .Builder()
            .setStrategy(Strategy.P2P_POINT_TO_POINT)
            .build()

        Nearby.getConnectionsClient(this)
            .startAdvertising(
                nickname, serviceId, connectionLifecycleCallback, advertisingOptions
            )
            .addOnSuccessListener {
                Log.i(this.localClassName, "advertising...")
            }
            .addOnFailureListener {
                Log.i(this.localClassName, "error occurred in advertising step", it)
            }
    }

    private fun startDiscovery() {
        val discoveryOptions =
            DiscoveryOptions.Builder()
                .setStrategy(Strategy.P2P_POINT_TO_POINT)
                .build()

        Nearby.getConnectionsClient(this)
            .startDiscovery(serviceId, endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener {
                Log.i(this.localClassName, "success discover!")
            }
            .addOnFailureListener {
                Log.i(this.localClassName, "error occurred in discovery step", it)
            }
    }

    private fun sendData(endpointId: String) {
        val bytesPayload =
            if (isHost) Payload.fromBytes(byteArrayOf("host".toByte()))
            else Payload.fromBytes(byteArrayOf("not host".toByte()))

        Nearby.getConnectionsClient(this).sendPayload(endpointId, bytesPayload)
    }
}