package com.sabgil.bbuckkugi.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.sabgil.bbuckkugi.common.Data.Failure
import com.sabgil.bbuckkugi.common.Data.Success
import com.sabgil.bbuckkugi.data.pref.AppSharedPreference
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import com.sabgil.bbuckkugi.presentation.ui.receive.ReceiveActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionService : LifecycleService() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    override fun onCreate() {
        super.onCreate()
        service = this
        startAdvertising()
    }

    private fun startAdvertising() {
        lifecycleScope.launch {
            connectionManager.startAdvertising(buildAdvertisingName())
                .collect {
                    when (it) {
                        is Success -> ReceiveActivity.startOnHome(this@ConnectionService, it.data)
                        is Failure -> restartAdvertising()
                    }
                }
        }
    }

    fun restartAdvertising() {
        lifecycleScope.launch {
            delay(RESTART_DELAY)
            startAdvertising()
        }
    }

    private fun buildAdvertisingName(): String {
        val name = requireNotNull(appSharedPreference.name)
        val gender = requireNotNull(appSharedPreference.gender).desc
        val profileImageUrl = appSharedPreference.profileImageUrl

        return if (profileImageUrl == null) {
            "$name|$gender"
        } else {
            "$name|$gender|$profileImageUrl"
        }
    }

    companion object {

        private const val RESTART_DELAY = 300L
        var service: ConnectionService? = null
    }
}