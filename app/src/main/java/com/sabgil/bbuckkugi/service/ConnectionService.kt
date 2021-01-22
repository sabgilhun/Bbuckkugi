package com.sabgil.bbuckkugi.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.TestActivity
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.common.ext.collectOnMain
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.repository.ConnectionManager
import com.sabgil.bbuckkugi.service.ConnectionService.Status.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionService : LifecycleService() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    private val errorHandler = CoroutineExceptionHandler { _, _ ->

    }

    private val backgroundDispatcher = errorHandler + Dispatchers.Default

    private val broadcastReceiver = ControlBroadCastReceiver()

    private var status: Status = None
        set(value) {
            field = value
            clearPreviousJob()
            startJob(status)
        }

    private var previousJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        status = Advertising
        registerLocalBroadCast()
    }

    override fun onDestroy() {
        unregisterLocalBroadCast()
        super.onDestroy()
    }

    private fun registerLocalBroadCast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadcastReceiver,
                IntentFilter().apply {
                    addAction(START_DISCOVERING)
                    addAction(STOP_DISCOVERING)
                    addAction(START_CONNECTION)
                }
            )
    }

    private fun unregisterLocalBroadCast() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)
    }

    private fun clearPreviousJob() {
        previousJob?.cancel()
    }

    private fun startJob(toBe: Status) {
        lifecycleScope.launch(backgroundDispatcher) {
            when (toBe) {
                None -> {
                }
                Advertising -> startAdvertising()
                Discovering -> startDiscovering()
                is Connecting -> startConnecting(toBe.endpointId)
            }
        }
    }

    private suspend fun startAdvertising() {
        connectionManager.startAdvertising(requireNotNull(appSharedPreference.nickname))
            .collectOnMain {
                when (it) {
                    is Result.Success -> {
                    }
                    is Result.Failure -> {
                        status = Advertising
                    }
                }
            }
    }

    private suspend fun startDiscovering() {
        connectionManager.startDiscovery()
            .collectOnMain {
                when (it) {
                    is Result.Success -> sendDiscoveredEndpoint(it.result)
                    is Result.Failure -> {
                        // TODO: 연결 종료 브로드캐스트 필요
                        status = Advertising
                    }
                }
            }
    }

    private suspend fun startConnecting(endpointId: String) {
        connectionManager.connectRemote(endpointId)
            .collectOnMain {
                when (it) {
                    is Result.Success -> {
                    }
                    is Result.Failure -> {
                        // TODO: 연결 종료 브로드캐스트 필요
                        status = Advertising
                    }
                }
            }
    }

    // TODO: activity 변경 필요
    private fun startActivity() {
        startActivity(Intent(this, TestActivity::class.java))
    }

    private fun sendDiscoveredEndpoint(discoveredEndpoint: DiscoveredEndpoint) {
        val intent = Intent(DISCOVERED_ENDPOINT).apply {
            putExtra("discoveredEndpoint", discoveredEndpoint)
        }

        LocalBroadcastManager
            .getInstance(this)
            .sendBroadcast(intent)
    }

    private inner class ControlBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                START_DISCOVERING -> status = Discovering
                STOP_DISCOVERING -> status = Advertising
                START_CONNECTION -> status =
                    Connecting(requireNotNull(intent.getStringExtra("endpointId")))
            }
        }
    }

    private sealed class Status {
        object None : Status()
        object Advertising : Status()
        object Discovering : Status()
        class Connecting(val endpointId: String) : Status()
    }

    companion object {

        const val DISCOVERED_ENDPOINT = "DISCOVERED_ENDPOINT"

        private const val START_DISCOVERING = "START_DISCOVERING"
        private const val STOP_DISCOVERING = "STOP_DISCOVERING"
        private const val START_CONNECTION = "START_CONNECTION"

        fun sendStartDiscoveringAction(context: Context) {
            LocalBroadcastManager
                .getInstance(context)
                .sendBroadcast(Intent(START_DISCOVERING))
        }

        fun sendStopDiscoveringAction(context: Context) {
            LocalBroadcastManager
                .getInstance(context)
                .sendBroadcast(Intent(START_DISCOVERING))
        }

        fun sendStartConnection(context: Context, endpointId: String) {
            LocalBroadcastManager
                .getInstance(context)
                .sendBroadcast(
                    Intent(START_DISCOVERING).apply { putExtra("endpointId", endpointId) }
                )
        }
    }
}