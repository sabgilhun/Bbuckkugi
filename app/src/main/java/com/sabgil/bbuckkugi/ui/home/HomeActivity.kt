package com.sabgil.bbuckkugi.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.checkSelfPermissionCompat
import com.sabgil.bbuckkugi.common.ext.requestPermissionsCompat
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityHomeBinding
import com.sabgil.bbuckkugi.service.AdvertisingService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeViewModel by viewModelOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        binding.advertise.setOnClickListener {
            LocalBroadcastManager
                .getInstance(this)
                .sendBroadcast(Intent(AdvertisingService.RE_START_ADVERTISING_ACTION))
        }

        binding.discovery.setOnClickListener {
            viewModel.startDiscovery()
        }

        binding.send.setOnClickListener {
            viewModel.sendData()
        }

        if (!isEnableBle()) {
            finish()
        }

        if (!hasPermissions()) {
            requestPermissionsCompat(needsPermissions, PERMISSION_REQUEST_CODE)
        }

        startService(Intent(this, AdvertisingService::class.java))
    }

    private fun isEnableBle() =
        packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    private fun hasPermissions(): Boolean {
        for (permission in needsPermissions) {
            if (checkSelfPermissionCompat(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001

        private val needPermissionsUnderQ: Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        private val needPermissionsOverQ: Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        private val needsPermissions =
            if (Build.VERSION.SDK_INT < 29) needPermissionsUnderQ else needPermissionsOverQ

        fun startOnTop(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}