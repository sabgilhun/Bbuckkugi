package com.sabgil.bbuckkugi.presentation.ui.start

import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.os.Build
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.checkSelfPermissionCompat
import com.sabgil.bbuckkugi.common.ext.requestPermissionsCompat
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityStartBinding
import com.sabgil.bbuckkugi.presentation.ui.guide.GuideActivity
import com.sabgil.bbuckkugi.presentation.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>(R.layout.activity_start) {

    private val viewModel by viewModelOf<StartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isEnableBle()) {
            finish()
        }

        if (!hasPermissions()) {
            requestPermissionsCompat(needsPermissions, PERMISSION_REQUEST_CODE)
        } else {
            viewModel.checkRequiredUserData()
        }

        viewModel.isExistRequiredData.observeNonNull {
            if (it) {
                HomeActivity.startOnTop(this)
            } else {
                GuideActivity.startOnTop(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.any { it == PERMISSION_DENIED }) {
            finish()
        } else {
            viewModel.checkRequiredUserData()
        }
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
    }
}