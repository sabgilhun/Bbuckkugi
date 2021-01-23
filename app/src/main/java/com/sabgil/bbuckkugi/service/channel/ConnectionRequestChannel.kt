package com.sabgil.bbuckkugi.service.channel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.common.Result

object ConnectionRequestChannel {
    private const val RECEIVE_RESULT = "RECEIVE_RESULT"
    private const val RECEIVE_RESULT_INTENT_TAG = "RECEIVE_RESULT_INTENT_TAG"

    private const val RECEIVE_ACTION = "RECEIVE_ACTION"
    private const val RECEIVE_ACTION_INTENT_TAG = "RECEIVE_ACTION_INTENT_TAG"

    fun registerClient(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onReceive: (Result<Nothing>) -> Unit
    ) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val result = intent.getSerializableExtra(RECEIVE_RESULT_INTENT_TAG)
                    @Suppress("UNCHECKED_CAST")
                    onReceive(result as Result<Nothing>)
                }
            }

            LocalBroadcastManager.getInstance(context)
                .registerReceiver(
                    receiver,
                    IntentFilter().apply { addAction(RECEIVE_RESULT) }
                )

            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    LocalBroadcastManager.getInstance(context)
                        .unregisterReceiver(receiver)
                    super.onDestroy(owner)
                }
            })
        }
    }

    fun registerHost(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onReceive: (String) -> Unit
    ) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val endpoint = requireNotNull(intent.getStringExtra(RECEIVE_ACTION_INTENT_TAG))
                    onReceive(endpoint)
                }
            }

            LocalBroadcastManager.getInstance(context)
                .registerReceiver(
                    receiver,
                    IntentFilter().apply { addAction(RECEIVE_ACTION) }
                )

            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    LocalBroadcastManager.getInstance(context)
                        .unregisterReceiver(receiver)
                    super.onDestroy(owner)
                }
            })
        }
    }

    fun sendActionForConnection(context: Context, endpointId: String) {
        LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(Intent(RECEIVE_ACTION).apply {
                putExtra(RECEIVE_ACTION_INTENT_TAG, endpointId)
            })
    }

    fun sendResult(context: Context, result: Result<Nothing>) {
        LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(Intent(RECEIVE_RESULT).apply {
                putExtra(RECEIVE_RESULT_INTENT_TAG, result)
            })
    }
}