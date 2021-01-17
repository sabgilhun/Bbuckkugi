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
import com.sabgil.bbuckkugi.repository.ConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AdvertisingService : LifecycleService() {

    private val errorHandler = CoroutineExceptionHandler { _, _ ->

    }

    private val backgroundDispatcher = errorHandler + Dispatchers.Default

    @Inject
    lateinit var connectionManager: ConnectionManager

    private var advertisingJob: Job? = null

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            advertisingJob?.let {
                it.cancel()
                startAdvertising()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerLocalBroadCast()
        startAdvertising()
    }

    override fun onDestroy() {
        unregisterLocalBroadCast()
        super.onDestroy()
    }

    private fun startAdvertising() {
        advertisingJob = lifecycleScope.launch(backgroundDispatcher) {
            delay(1000)
            // TODO: 저장된 이름 사용하도록 수정 필요
            connectionManager.startAdvertising("test")
                .collect {
                    withContext(Dispatchers.Main) {
                        when (it) {
                            is Result.Success -> startActivity()
                            is Result.Failure -> startAdvertising()
                        }
                    }
                }
        }
    }

    private fun registerLocalBroadCast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadcastReceiver,
                IntentFilter().apply { addAction(RE_START_ADVERTISING_ACTION) }
            )
    }

    private fun unregisterLocalBroadCast() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)
    }

    // TODO: activity 변경 필요
    private fun startActivity() {
        startActivity(Intent(this, TestActivity::class.java))
    }

    companion object {

        const val RE_START_ADVERTISING_ACTION = "RE_START_ADVERTISING_ACTION"
    }
}