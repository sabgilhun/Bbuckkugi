package com.sabgil.bbuckkugi.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.repository.ConnectionManager
import kotlinx.coroutines.currentCoroutineContext

class HomeViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    fun startAdvertising(hostName: String) {
        connectionManager.startAdvertising(hostName)
            .collectResult {
                success {
                    Log.i("coroutine test", currentCoroutineContext().toString())
                }
                error {
                    Log.i("coroutine test", currentCoroutineContext().toString())
                }
            }
    }

    fun startDiscovery() {
        ioScopeLaunch {
            connectionManager.startDiscovery()
                .collectResult {
                    success {
                        Log.i("coroutine test", currentCoroutineContext().toString())
                    }
                    error {
                        Log.i("coroutine test", currentCoroutineContext().toString())
                    }
                }
        }
    }
}