package com.sabgil.bbuckkugi

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*


class MainActivity : AppCompatActivity() {

    private val serviceId = "My Unique value"
    private val nickname = "nickname"

    private var isHost = true

    private val handler = Handler()

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val byteArray = payload.asBytes() ?: return

            Log.i(
                this@MainActivity.localClassName,
                "received: " + String(byteArray)
            )
            handler.postDelayed({ sendData((endpointId)) }, 3000)
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
            if (p1.serviceId != serviceId) {
                Log.i(this@MainActivity.localClassName, "wrong service id: $endpointId")
                return
            }
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

        findViewById<View>(R.id.button).setOnClickListener {
            isHost = false
            startDiscovery()
        }

        if (!isEnableBle()) {
            finish()
        }

        if (!hasPermissions()) {
            requestPermissionsCompat(needsPermissions, PERMISSION_REQUEST_CODE)
        }

        startAdvertising()
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
            if (isHost) Payload.fromBytes("host".toByteArray())
            else Payload.fromBytes("client".toByteArray())

        Nearby.getConnectionsClient(this).sendPayload(endpointId, bytesPayload)
    }

    private val needPermissionsUnderQ: Array<String> = arrayOf(
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        ACCESS_WIFI_STATE,
        CHANGE_WIFI_STATE,
        ACCESS_COARSE_LOCATION
    )

    private val needPermissionsOverQ: Array<String> = arrayOf(
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        ACCESS_WIFI_STATE,
        CHANGE_WIFI_STATE,
        ACCESS_FINE_LOCATION
    )

    private val needsPermissions =
        if (Build.VERSION.SDK_INT < 29) needPermissionsUnderQ else needPermissionsOverQ

    private fun hasPermissions(): Boolean {
        for (permission in needsPermissions) {
            if (checkSelfPermissionCompat(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private val PERMISSION_REQUEST_CODE = 1001

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    finish()
                    return
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission)

    private fun AppCompatActivity.requestPermissionsCompat(
        permissionsArray: Array<String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
    }

    private fun isEnableBle() =
        packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

}