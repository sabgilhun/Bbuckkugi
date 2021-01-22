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

    private var status: Status = NONE
        set(value) {
            field = value
            clearPreviousJob()
            startJob(status)
        }

    private var previousJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        status = ADVERTISING
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
        if (toBe == NONE) return

        lifecycleScope.launch(backgroundDispatcher) {
            when (toBe) {
                NONE -> {
                    // do not anything
                }
                ADVERTISING -> {
                    startAdvertising()
                }
                DISCOVERING -> {
                    startDiscovering()
                }
                CONNECTING -> {
                    // TODO: 인텐트에서 값 꺼내서 쓰기
                    startConnecting("")
                }
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
                        status = ADVERTISING
                    }
                }
            }
    }

    private suspend fun startDiscovering() {
        connectionManager.startDiscovery()
            .collectOnMain {
                when (it) {
                    is Result.Success -> {
                    }
                    is Result.Failure -> {
                        // TODO: 연결 종료 브로드캐스트 필요
                        status = ADVERTISING
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
                        status = ADVERTISING
                    }
                }
            }
    }

    // TODO: activity 변경 필요
    private fun startActivity() {
        startActivity(Intent(this, TestActivity::class.java))
    }

    private inner class ControlBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                START_DISCOVERING -> status = DISCOVERING
                STOP_DISCOVERING -> status = ADVERTISING
                START_CONNECTION -> status = CONNECTING
            }
        }
    }

    private enum class Status {
        NONE, ADVERTISING, DISCOVERING, CONNECTING
    }

    companion object {

        const val RE_START_ADVERTISING_ACTION = "RE_START_ADVERTISING_ACTION"
        const val START_DISCOVERING = "START_DISCOVERING"
        const val STOP_DISCOVERING = "STOP_DISCOVERING"
        const val START_CONNECTION = "START_CONNECTION"
    }
}